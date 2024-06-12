package com.info_hub.repositories.article;

import com.info_hub.enums.Status;
import com.info_hub.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends ArticleRepositoryCustom, JpaRepository<Article, Integer> {
    Page<Article> findByUsers_Id(Integer id, Pageable pageable);

    Article findByIdAndStatus(Integer id, Status status);

    boolean existsByIdAndUsers_Id(Integer articleId, Integer userId);

    Page<Article> findByTags_Code(String code, Pageable pageable);

    Page<Article> findByCategory_Code(String code, Pageable pageable);

    Page<Article>  findByCategory_ParentCategory_Id(Integer id, Pageable pageable );

    long countByStatus(Status status);




}
