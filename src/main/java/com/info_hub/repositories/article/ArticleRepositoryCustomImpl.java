package com.info_hub.repositories.article;

import com.info_hub.constant.QueryConstant;
import com.info_hub.models.Article;
import com.info_hub.dtos.responses.SimpleResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SimpleResponse<Article> findByCondition(Map<String, String> params, Pageable pageable) {
        int limit = pageable.getPageSize();
        int pageSize = pageable.getPageNumber() + 1;

        StringBuilder sql = new StringBuilder(buildQueryFilter(params))
                .append(" ORDER BY a.created_date DESC")
                .append(" LIMIT ").append(limit)
                .append(" OFFSET ").append(pageable.getOffset());
        // debug sql
        System.out.println(sql.toString());
        // excute query
        Query query = entityManager.createNativeQuery(sql.toString(), Article.class);
        List<Article> articles = query.getResultList();

        // result
        int countItems = query.getResultList().size();
        SimpleResponse<Article> result = new SimpleResponse<>();
        result.data = articles;
        result.page = pageSize;
        result.limit = limit;
        result.totalItems = countItems;
        result.totalPage = (int) Math.ceil((double) countItems / pageSize);

        return result;
    }


    private String buildQueryFilter(Map<String, String> params) {
        StringBuilder sql = new StringBuilder(QueryConstant.SELECT_FROM_ARTICLE)
                .append(QueryConstant.WHERE_ONE_EQUAL_ONE);
        sql = buildSqlCommon(params, sql);
        return sql.toString();
    }


    private StringBuilder buildSqlCommon(Map<String, String> params, StringBuilder sql) {
        String categoryId = params.get("f_category_id");
        String dateTo = params.get("f_date_to"); // 07/03/2024
        String createdBy = params.get("f_created_by");
        String reviewerId = params.get("f_reviewer_id");
        String title = params.get("f_title");
        String dateFrom = params.get("f_date_from"); // 13/08/2002
        String status = params.get("s_status"); // REJECTED, PENDING, APPROVE
        String tagIds = params.get("f_tag_ids"); // "1,2,3,4"

        if (tagIds != null) {
            if (!tagIds.isEmpty()) {
                queryTagFilter(tagIds, sql);
            }
        }

        if (title != null) {
            if (!title.isEmpty()) {
                sql.append(" and a.title like '%" + title + "%'");
            }
        }

        if (reviewerId != null) {
            if (!reviewerId.isEmpty()) {
                sql.append(String.format(" and a.reviewer_id = %s", reviewerId));
            }
        }

        if (createdBy != null) {
            if (!createdBy.isEmpty()) {
                sql.append(String.format(" and a.created_by =  %s", createdBy));
            }
        }

        if (categoryId != null) {
            if (!categoryId.isEmpty()) {
                sql.append(String.format(" and a.category_id =  %s", categoryId));
            }
        }

        if (status != null) {
            if (!status.isEmpty()) {
                sql.append(String.format(" and a.status =  '%s'", status));
            }
        }

        if (dateFrom != null) {
            if (!dateFrom.isEmpty()) {
                sql.append(String.format(" and created_date >= '%s'", dateFrom));
            }
        }

        if (dateTo != null) {
            if (!dateTo.isEmpty()) {
                sql.append(String.format(" and created_date <= '%s'", dateTo));
            }
        }

        return sql;
    }

    private StringBuilder queryTagFilter(String tagIds, StringBuilder sql) {
        // get article id from tag ids
        String queryArticleByTagId = String
                .format("select article_id from article_tag where tag_id in (%s) group by article_id", tagIds);

        Query query = entityManager.createNativeQuery(queryArticleByTagId, Integer.class);
        // list article id
        List<Integer> articleIds = query.getResultList();

        // return no record if not found
        String whereClause = " and 1=2";

        if(!articleIds.isEmpty()) {
            String articleIdsString = articleIds.stream()
                    .map(Object::toString) // Convert each Integer to String
                    .collect(Collectors.joining(",")); // Join with commas
            whereClause = String.format(" and a.id in (%s)", articleIdsString);
        }
        return sql.append(whereClause);
    }


}
