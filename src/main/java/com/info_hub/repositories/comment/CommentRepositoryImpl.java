package com.info_hub.repositories.comment;

import com.info_hub.constant.QueryConstant;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.models.Comment;
import com.info_hub.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SimpleResponse<Comment> findByCondition(Map<String, String> params, Pageable pageable) {
        int limit = pageable.getPageSize();
        int pageSize = pageable.getPageNumber() + 1;

        StringBuilder sql = new StringBuilder(buildQueryFilter(params))
                .append(QueryConstant.LIMIT).append(limit)
                .append(QueryConstant.OFFSET).append(pageable.getOffset());

        // excute query
        Query query = entityManager.createNativeQuery(sql.toString(), Comment.class);
        List<Comment> comments = query.getResultList();

        // result
        int countItems = query.getResultList().size();
        SimpleResponse<Comment> result = new SimpleResponse<>();
        result.data = comments;
        result.page = pageSize;
        result.limit = limit;
        result.totalItems = countItems;
        result.totalPage = (int) Math.ceil((double) countItems / pageSize);

        return result;
    }


    private String buildQueryFilter(Map<String, String> params) {
        StringBuilder sql = new StringBuilder(QueryConstant.SELECT_FROM_COMMENT);
        sql = buildSqlCommon(params, sql);
        return sql.toString();
    }

    private StringBuilder buildSqlCommon(Map<String, String> params, StringBuilder sql) {
        String email = params.get("email");
        String text = params.get("text");
        String status = params.get("status");

        if (email != null) {
            if (!email.isEmpty()) {
                sql.append(" and user.email like '%" + email + "%'");
            }
        }

        if (text != null) {
            if (!text.isEmpty()) {
                sql.append(" and c.text like '%" + text + "%'");
            }
        }

        if (status != null) {
            if (!status.isEmpty()) {
                sql.append(String.format(" and c.status = '%s'", status));
            }
        }

        return sql;
    }
}
