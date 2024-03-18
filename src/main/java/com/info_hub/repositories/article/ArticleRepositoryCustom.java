package com.info_hub.repositories.article;

import com.info_hub.models.Article;
import com.info_hub.dtos.responses.SimpleResponse;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ArticleRepositoryCustom {
    SimpleResponse<Article> findByCondition(Map<String, String> params, Pageable pageable);
}
