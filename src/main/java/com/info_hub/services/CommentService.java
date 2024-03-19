package com.info_hub.services;

import com.info_hub.components.GetPageableUtil;
import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.comment.CommentDTO;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.comment.DetailCommentResponse;
import com.info_hub.dtos.responses.comment.ListCommentResponse;
import com.info_hub.enums.Status;
import com.info_hub.exceptions.BadRequestException;
import com.info_hub.models.Article;
import com.info_hub.models.Comment;
import com.info_hub.models.User;
import com.info_hub.repositories.article.ArticleRepository;
import com.info_hub.repositories.comment.CommentRepository;
import com.info_hub.services.auth.ProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    /**
     * Get all comment from all article
     *
     * @param params: email, text, status
     * @return a list after filter by params.
     */
    public SimpleResponse<ListCommentResponse> getListComment(Map<String, String> params) {
        Pageable pageable = GetPageableUtil.getPageable(params);
        SimpleResponse<Comment> entitiesResponse = commentRepository.findByCondition(params, pageable);

        List<ListCommentResponse> results = new ArrayList<>();
        for (Comment comment : entitiesResponse.getData()) {
            ListCommentResponse item = modelMapper.map(comment, ListCommentResponse.class);
            results.add(item);
        }

        return SimpleResponse.<ListCommentResponse>builder()
                .data(results)
                .page(entitiesResponse.getPage())
                .limit(entitiesResponse.getLimit())
                .totalItems(entitiesResponse.getTotalItems())
                .totalPage(entitiesResponse.getTotalPage())
                .build();
    }

    public ResponseMessage requestComment(Integer articleId, CommentDTO commentDTO) {
        // find article
        Article articleToComment = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new BadRequestException("Article not found with id: " + articleId));

        // get current user
        User currentUser = ProfileService.getLoggedInUser();

        // save comment
        Comment newComment = Comment.builder()
                .article(articleToComment)
                .user(currentUser)
                .status(Status.PENDING) // means that the comment is not showing on the article.
                .text(commentDTO.getText())
                .build();

        commentRepository.save(newComment);

        return new ResponseMessage("Bình luận đang được xét duyệt...");
    }

    public ResponseMessage reviewComment(Integer commentId, Status status) {
        Comment updateComment = findCommentById(commentId);

        // who is reviewer ?
        User currentUser = ProfileService.getLoggedInUser();
        updateComment.setReviewer(currentUser);

        // set status
        updateComment.setStatus(status); // PENDING, REJECTED, APPROVED

        commentRepository.save(updateComment);
        return ResponseMessage.success();
    }

    public DetailCommentResponse getDetailComment(Integer commentId) {
        Comment currentComment = findCommentById(commentId);
        return modelMapper.map(currentComment,DetailCommentResponse.class);
    }

    public Comment findCommentById(Integer commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("Comment not found with id : " + commentId));
    }

    public ResponseMessage deleteComment(Integer commentId) {
        commentRepository.delete(findCommentById(commentId));
        return ResponseMessage.success();
    }


}
