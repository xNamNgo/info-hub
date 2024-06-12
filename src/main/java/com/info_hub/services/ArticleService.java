package com.info_hub.services;

import com.info_hub.components.GetPageableUtil;
import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.ReviewDTO;
import com.info_hub.dtos.article.ArticleDTO;
import com.info_hub.dtos.home.ArticleResponse;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.article.ArticleDetailResponse;
import com.info_hub.dtos.responses.article.ArticleListResponse;
import com.info_hub.dtos.responses.article.ArticleSaved;
import com.info_hub.dtos.responses.article.UserResponse;
import com.info_hub.dtos.responses.category.CategoryNodeResponse;
import com.info_hub.dtos.responses.tag.TagResponse;
import com.info_hub.enums.Status;
import com.info_hub.exceptions.BadRequestException;
import com.info_hub.models.*;
import com.info_hub.repositories.CategoryRepository;
import com.info_hub.repositories.ImageRepository;
import com.info_hub.repositories.TagRepository;
import com.info_hub.repositories.article.ArticleRepository;
import com.info_hub.repositories.user.UserRepository;
import com.info_hub.services.auth.ProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository; // find thumbnail image
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

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
        List<ArticleListResponse> result = entitiesResponse.getData().stream()
                .map(comment -> modelMapper.map(comment, ArticleListResponse.class))
                .toList();

        return SimpleResponse.<ArticleListResponse>builder()
                .data(result)
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
                .status(Status.PENDING)
                .build();

        // who did created article?
        User author = ProfileService.getLoggedInUser();
        newArticle.setAuthor(author);

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
        existingArticle.setStatus(Status.PENDING);
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
    public ResponseMessage reviewArticle(Integer articleId, ReviewDTO status) {
        Article existingArticle = findExistingArticleById(articleId);

        // how to know who is appprove ?
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User reviewer = (User) authentication.getPrincipal();

        existingArticle.setReviewer(reviewer);

        switch (status.getStatus()) {
            case APPROVED:
                existingArticle.setStatus(Status.APPROVED);
                break;
            case REJECTED:
                // reason when rejected
                existingArticle.setRejectedMessage(status.getMessage());
                existingArticle.setStatus(Status.REJECTED);
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
    public ArticleSaved saveArticle(Integer articleId) {
        User loggedInUser = ProfileService.getLoggedInUser();
        Article articleToSave = findExistingArticleById(articleId);

        // check if article id exist in user that mean was saved
        // so need to unsave
        boolean isSaved = false;
        if (!articleToSave.isSavedArticle(loggedInUser)) {
            articleToSave.addSavedArticle(loggedInUser);
            isSaved = true;
        } else {
            articleToSave.removeSavedArticle(loggedInUser);
        }

        articleRepository.save(articleToSave);
        return new ArticleSaved(isSaved);
    }

    public ArticleSaved checkArticleSave(Integer articleId) {
        User loggedInUser = ProfileService.getLoggedInUser();
        boolean isArticleSaved = articleRepository.existsByIdAndUsers_Id(articleId, loggedInUser.getId());
        return new ArticleSaved(isArticleSaved);
    }

    /**
     * Homepage API
     *
     * @param params
     * @return list article by tag code.
     */
    public SimpleResponse<ArticleResponse> searchArticleByTagCode(Map<String, String> params) {
        String tagCode = params.get("code");
        String categoryCode = params.get("c_code");
        Pageable pageable = GetPageableUtil.getPageable(params);

        Page<Article> articles = findArticles(tagCode, categoryCode, pageable);

        List<ArticleResponse> result = articles.stream()
                .map(articleEntity -> modelMapper.map(articleEntity, ArticleResponse.class))
                .toList();

        return SimpleResponse.<ArticleResponse>builder()
                .data(result)
                .page((articles.getNumber() + 1))
                .limit(articles.getSize())
                .totalItems((int) articles.getTotalElements())
                .totalPage(articles.getTotalPages())
                .build();

    }

    private Page<Article> findArticles(String tagCode, String categoryCode, Pageable pageable) {
        if (tagCode != null) {
            return articleRepository.findByTags_Code(tagCode, pageable);
        }

        Category category = categoryRepository.findByCode(categoryCode);
        if (category == null) {
            throw new BadRequestException("Category not found.");
        }

        return category.isHasParent() ?
                articleRepository.findByCategory_Code(categoryCode, pageable) :
                articleRepository.findByCategory_ParentCategory_Id(category.getId(), pageable);
    }


}
