package com.info_hub.services;

import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.article.ArticleDTO;
import com.info_hub.repositories.ArticleRepository;
import com.info_hub.responses.ListResponse;
import com.info_hub.responses.article.ArticleListResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleService extends BaseService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;


    public ArticleService(ArticleRepository articleRepository,
                          ModelMapper modelMapper) {
        super(articleRepository);
        this.modelMapper = modelMapper;
        this.articleRepository = articleRepository;
    }

    public ListResponse<ArticleListResponse> getAllArticles(Map<String, String> params) {
        ListResponse<ArticleListResponse> entityResponse = this.getAll(params);

        // convert entity data to dto data
        List<ArticleListResponse> data = entityResponse.getData().stream()
                .map(entity -> modelMapper.map(entity, ArticleListResponse.class))
                .toList();

        return ListResponse.<ArticleListResponse>builder()
                .data(data)
                .page(entityResponse.getPage())
                .limit(entityResponse.getLimit())
                .totalPage(entityResponse.getTotalPage())
                .totalItems(entityResponse.getTotalItems())
                .build();
    }

    public ResponseMessage createArticle(ArticleDTO articleDTO) {


        return ResponseMessage.success();

//        int[] tagIds = entity.getTagIds();
//        Integer categoryId = entity.getCategoryId();
//
//        // add category
//        Category category = categoryRepository.findById(categoryId).get();
//        entity.addCategory(category);
//
//        // add tags
//        List<Tag> tags = articleRepository.getTags(tagIds);
//
//        tags.forEach(item -> entity.addTag(item));
//        tags.forEach(entity::addTag);
//        return baseRepository.save(entity);
    }

}
