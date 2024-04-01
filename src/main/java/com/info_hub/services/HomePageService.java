package com.info_hub.services;

import com.info_hub.components.GetPageableUtil;
import com.info_hub.dtos.home.ArticleResponse;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.home.ArticleComments;
import com.info_hub.dtos.responses.home.DetailArticleHome;
import com.info_hub.enums.Status;
import com.info_hub.exceptions.BadRequestException;
import com.info_hub.models.Article;
import com.info_hub.models.Comment;
import com.info_hub.repositories.article.ArticleRepository;
import com.info_hub.repositories.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;

    public SimpleResponse<ArticleResponse> fetchHomeData(Map<String, String> params) {
        Pageable pageable = GetPageableUtil.getPageable(params);
        // findByCondition() <- invoke from ArticleRepositoryCustomImpl
        SimpleResponse<Article> entitiesResponse = articleRepository.findByCondition(params, pageable);

        List<ArticleResponse> result = entitiesResponse.getData().stream()
                .map(articleEntity -> modelMapper.map(articleEntity, ArticleResponse.class))
                .toList();

        return SimpleResponse.<ArticleResponse>builder()
                .data(result)
                .page(entitiesResponse.getPage())
                .limit(entitiesResponse.getLimit())
                .totalItems(entitiesResponse.getTotalItems())
                .totalPage(entitiesResponse.getTotalPage())
                .build();
    }

    /**
     * get data article by id and status is approved
     *
     * @param articleId
     * @return
     */

    public DetailArticleHome getDetailArticle(Integer articleId) {
        Article articleEntity = articleRepository.findByIdAndStatus(articleId, Status.APPROVED);

        if (articleEntity != null) {
            return modelMapper.map(articleEntity, DetailArticleHome.class);
        }
        // else
        throw new BadRequestException("Article not found with id: " + articleId);
    }

    /**
     * get all comments from articleId which is approved
     *
     * @param articleId
     * @param params:   limit: 5, page: 1, sort:"asc"
     * @return
     */
    public SimpleResponse<ArticleComments> getAllCommentFromArticle(Integer articleId, Map<String, String> params) {
        Pageable pageable = GetPageableUtil.getPageable(params);
        Page<Comment> entitiesResponse = commentRepository.findByStatusAndArticle_Id(Status.APPROVED, articleId, pageable);

        List<ArticleComments> commentsList = entitiesResponse.getContent().stream()
                .map((element) -> modelMapper.map(element, ArticleComments.class))
                .toList();

        return SimpleResponse.<ArticleComments>builder()
                .data(commentsList)
                .page(pageable.getPageNumber() + 1)
                .limit(pageable.getPageSize())
                .totalItems((int) entitiesResponse.getTotalElements())
                .totalPage(entitiesResponse.getTotalPages())
                .build();
    }


}
