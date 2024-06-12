package com.info_hub.services.auth;

import com.info_hub.components.GetPageableUtil;
import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.auth.ProfileDTO;
import com.info_hub.dtos.auth.ResetPasswordDTO;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.article.SavedArticleResponse;
import com.info_hub.dtos.responses.user.AllMyCommentResponse;
import com.info_hub.dtos.responses.user.ProfileResponse;
import com.info_hub.dtos.responses.user.UploadAvatarResponse;
import com.info_hub.enums.Status;
import com.info_hub.models.Article;
import com.info_hub.models.Comment;
import com.info_hub.models.Image;
import com.info_hub.models.User;
import com.info_hub.repositories.TokenRepository;
import com.info_hub.repositories.article.ArticleRepository;
import com.info_hub.repositories.comment.CommentRepository;
import com.info_hub.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    @Value("${api.url-image}")
    private String ipImage;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;


    public ProfileResponse getMyProfile() {
        User user = getLoggedInUser();

        String imgUrl = user.getImage() != null ?
                (user.getImage().getUrl()) :
                (ipImage + "default-avatar.png");

        return ProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .image(imgUrl)
                .build();
    }

    public UploadAvatarResponse uploadAvatar(Image file) {
        User user = getLoggedInUser();
        // Set the image for the existing user, then update.
        user.setImage(file);
        userRepository.save(user);

        return new UploadAvatarResponse(user.getImage().getUrl());
    }

    public ResponseMessage updateProfile(ProfileDTO params) {
        User user = getLoggedInUser();

        user.setFullName(params.getFullName());
//        String userEmail = user.getEmail();
//        if (!params.getEmail().equals(userEmail)) {
//            boolean isExistEmail = userRepository.existsByEmail(params.getEmail());
//            if (!isExistEmail) {
//                user.setEmail(params.getEmail());
//            } else {
//                throw new BadRequestException("An email existed in another user");
//            }
//        }

        // update user
        userRepository.save(user);

        return ResponseMessage.success();
    }

    public ResponseMessage changePassword(ResetPasswordDTO req) {
        User user = getLoggedInUser();
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // revoke token when change password successful
        user.revokeTokens();

        userRepository.save(user);


        return ResponseMessage.success();
    }

    public static User getLoggedInUser() {
        // get user details from Jwt Filter class. (SecurityContext)
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public SimpleResponse<SavedArticleResponse> getArticleSaved(Map<String, String> params) {
        User user = getLoggedInUser();

        Pageable pageable = GetPageableUtil.getPageable(params);
        Page<Article> savedArticle = articleRepository.findByUsers_Id(user.getId(), pageable);

        // convert entity to DTO
        List<SavedArticleResponse> data = savedArticle.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, SavedArticleResponse.class)).toList();

        return SimpleResponse.<SavedArticleResponse>builder()
                .data(data)
                .page(pageable.getPageNumber() + 1)
                .limit(pageable.getPageSize())
                .totalPage(savedArticle.getTotalPages())
                .totalItems((int) savedArticle.getTotalElements())
                .build();
    }

    public SimpleResponse<AllMyCommentResponse> getAllMyComment(Map<String, String> params) {
        User currentUser = getLoggedInUser();
        Pageable pageable = GetPageableUtil.getPageable(params);
        Page<Comment> myComments = commentRepository
                .findByUser_IdAndStatus(currentUser.getId(), Status.APPROVED, pageable);


        List<AllMyCommentResponse> data = myComments.stream()
                .map(comment -> modelMapper.map(comment, AllMyCommentResponse.class))
                .collect(Collectors.toList());

        return SimpleResponse.<AllMyCommentResponse>builder()
                .data(data)
                .page(pageable.getPageNumber() + 1)
                .limit(pageable.getPageSize())
                .totalPage(myComments.getTotalPages())
                .totalItems((int) myComments.getTotalElements())
                .build();
    }


}
