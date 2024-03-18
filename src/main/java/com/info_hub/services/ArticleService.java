package com.info_hub.services;

import com.info_hub.components.GetPageableUtil;
import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.article.ApproveArticleDTO;
import com.info_hub.dtos.article.ArticleDTO;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.article.ArticleDetailResponse;
import com.info_hub.dtos.responses.article.ArticleListResponse;
import com.info_hub.dtos.responses.article.UserResponse;
import com.info_hub.dtos.responses.category.CategoryNodeResponse;
import com.info_hub.dtos.responses.tag.TagResponse;
import com.info_hub.enums.ArticleStatus;
import com.info_hub.exceptions.BadRequestException;
import com.info_hub.models.*;
import com.info_hub.repositories.CategoryRepository;
import com.info_hub.repositories.ImageRepository;
import com.info_hub.repositories.TagRepository;
import com.info_hub.repositories.article.ArticleRepository;
import com.info_hub.repositories.user.UserRepository;
import com.info_hub.services.auth.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository; // find thumbnail image
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    public ArticleService(ArticleRepository articleRepository,
                          ModelMapper modelMapper,
                          ImageRepository imageRepository,
                          TagRepository tagRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Return list Article
     *
     * @param params: - For pagination: page, limit
     *                - For filters:
     * @return
     */
    public SimpleResponse<ArticleListResponse> getAllArticles(Map<String, String> params) {
        Pageable pageable = GetPageableUtil.getPageable(params);

        // findByCondition() <- invoke from ArticleRepositoryCustomImpl
        SimpleResponse<Article> entitiesResponse = articleRepository.findByCondition(params, pageable);

        //  convert List Entity to List DTO
        List<ArticleListResponse> results = new ArrayList<>();
        for (Article entity : entitiesResponse.getData()) {
            ArticleListResponse item = convertEntityToDTO(entity);
            results.add(item);
        }

        return SimpleResponse.<ArticleListResponse>builder()
                .data(results)
                .page(entitiesResponse.getPage())
                .limit(entitiesResponse.getLimit())
                .totalItems(entitiesResponse.getTotalItems())
                .totalPage(entitiesResponse.getTotalPage())
                .build();
    }

    /**
     * The default status when creating an article is "PENDING."
     * This means the article will only display if the status is "APPROVED."
     * When creating an article, the next step is that it needs to be approved
     * before it can be displayed on the list of articles.
     */
    public ResponseMessage createArticle(ArticleDTO articleDTO) {
        Article newArticle = Article.builder()
                .title(articleDTO.getTitle())
                .description(articleDTO.getDescription())
                .content(articleDTO.getContent())
                .status(ArticleStatus.PENDING)
                .build();

        saveThumbnailToArticle(newArticle, articleDTO.getThumbnailId());
        saveTagsToArticle(newArticle, articleDTO.getTagIds());
        saveCategoryToArticle(newArticle, articleDTO.getCategoryId());

        articleRepository.save(newArticle);

        return ResponseMessage.success();
    }


    public ResponseMessage updateArticle(Integer id, ArticleDTO articleDTO) {
        Article existingArticle = findExistingArticleById(id);
        existingArticle.setTitle(articleDTO.getTitle());
        existingArticle.setDescription(articleDTO.getDescription());
        existingArticle.setContent(articleDTO.getContent());
        existingArticle.setStatus(ArticleStatus.PENDING);
        saveThumbnailToArticle(existingArticle, articleDTO.getThumbnailId());
        saveTagsToArticle(existingArticle, articleDTO.getTagIds());
        saveCategoryToArticle(existingArticle, articleDTO.getCategoryId());
        articleRepository.save(existingArticle);
        return ResponseMessage.success();
    }

    public ArticleDetailResponse getArticleById(Integer id) {
        Article existingArticle = findExistingArticleById(id);

        User articleAuthor = userRepository.findById(existingArticle.getCreatedBy()).get();

        return convertToArticleDetails(existingArticle, articleAuthor);
    }

    private ArticleDetailResponse convertToArticleDetails(Article existingArticle, User author) {
        ArticleDetailResponse response = ArticleDetailResponse.builder()
                .articleId(existingArticle.getId())
                .authorName(author.getFullName())
                .title(existingArticle.getTitle())
                .description(existingArticle.getDescription())
                .thumbnailUrl(existingArticle.getThumbnail().getUrl())
                .content(existingArticle.getContent())
                .status(existingArticle.getStatus().toString())
                .message(existingArticle.getRejectedMessage())
                .build();

        // tags
        response.setTags(existingArticle
                .getTags()
                .stream()
                .map((element) ->
                        modelMapper.map(element, TagResponse.class)).toList()
        );

        // category
        response.setCategory(new CategoryNodeResponse(
                existingArticle.getCategory().getId(),
                existingArticle.getCategory().getName())
        );

        return response;
    }

    /**
     * When an article is approved, if the status is "APPROVED",
     * the article will be displayed in the list of articles.
     * Otherwise, if the status is "REJECTED", the article will be displayed in the list of rejected articles.
     */
    public ResponseMessage reviewArticle(Integer articleId, ApproveArticleDTO status) {
        Article existingArticle = findExistingArticleById(articleId);

        // how to know who is appprove ?
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User reviewer = (User) authentication.getPrincipal();

        existingArticle.setReviewer(reviewer);

        switch (status.getStatus()) {
            case APPROVED:
                existingArticle.setStatus(ArticleStatus.APPROVED);
                break;
            case REJECTED:
                // reason when rejected
                existingArticle.setRejectedMessage(status.getMessage());
                existingArticle.setStatus(ArticleStatus.REJECTED);
                break;
        }

        // udpate status
        articleRepository.save(existingArticle);
        return ResponseMessage.success();
    }

    private Article findExistingArticleById(Integer articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new BadRequestException("Article not found with id: " + articleId));
    }

    private void saveThumbnailToArticle(Article article, Integer thumbnailId) {
        Image thumbnail = imageRepository.findById(thumbnailId)
                .orElseThrow(() -> new BadRequestException("Image not found"));
        article.setThumbnail(thumbnail);
    }

    private void saveTagsToArticle(Article article, List<Integer> tagIds) {
        List<Tag> tags = tagRepository.getTags(tagIds);
        article.addAllTag(tags);
    }

    private void saveCategoryToArticle(Article article, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Category not found"));
        article.addCategory(category);
    }

    private String getNameByUserId(Integer personId) {
        User user = userRepository.findById(personId).get();
        return user.getFullName();
    }

    private ArticleListResponse convertEntityToDTO(Article entity) {
        ArticleListResponse result = ArticleListResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .categoryName(entity.getCategory().getName())
                .authorFullname(getNameByUserId(entity.getCreatedBy()))
                .updatedDate(entity.getCreatedDate())
                .status(entity.getStatus().toString())
                .build();

        // reviewerFullname(entity.getReviewer().getFullName())
        User reviewer = entity.getReviewer();
        if (reviewer != null) {
            result.setReviewerFullname(reviewer.getFullName());
        } else {
            result.setReviewerFullname("-");
        }
        return result;
    }

    public List<UserResponse> getListReviewers() {
        return userRepository.findByRole_Code(new String[]{"ROLE_ADMIN", "ROLE_COLLABORATOR"})
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getFullName()))
                .collect(Collectors.toList());
    }

    public List<UserResponse> getListAuthors() {
        return userRepository.findByRole_Code(new String[]{"ROLE_JOURNALIST"})
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getFullName()))
                .collect(Collectors.toList());
    }

    /**
     * User save article by click in button "save" in Front end.
     */
    public ResponseMessage saveArticle(Integer articleId) {
        User loggedInUser = ProfileService.getLoggedInUser();
        Article articleToSave = findExistingArticleById(articleId);

        // check if article id exist in user that mean was saved
        // so need to unsave
        if(!articleToSave.isSavedArticle(loggedInUser)) {
            articleToSave.addSavedArticle(loggedInUser);
        } else {
            articleToSave.removeSavedArticle(loggedInUser);
        }

        articleRepository.save(articleToSave);
        return ResponseMessage.success();
    }

}
