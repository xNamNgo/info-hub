package com.info_hub.repositories.user;

import com.info_hub.constant.QueryConstant;
import com.info_hub.models.User;
import com.info_hub.dtos.responses.SimpleResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SimpleResponse<User> findByCondition(Map<String, String> params, Pageable pageable) {
        int limit = pageable.getPageSize();
        int pageSize = pageable.getPageNumber() + 1;

        StringBuilder sql = new StringBuilder(buildQueryFilter(params))
                .append(QueryConstant.LIMIT).append(limit)
                .append(QueryConstant.OFFSET).append(pageable.getOffset());

        // excute query
        Query query = entityManager.createNativeQuery(sql.toString(), User.class);
        List<User> articles = query.getResultList();

        // result
        int totalItems = countTotalItem(params);
        SimpleResponse<User> result = new SimpleResponse<>();
        result.data = articles;
        result.page = pageSize;
        result.limit = limit;
        result.totalItems = totalItems;
        result.totalPage = (int) Math.ceil((double) totalItems / (double) limit);

        return result;
    }

    private int countTotalItem(Map<String, String> params) {
        Query countRow = entityManager.createNativeQuery(buildQueryFilter(params));
        return countRow.getResultList().size();
    }

    private String buildQueryFilter(Map<String, String> params) {
        StringBuilder sql = new StringBuilder(QueryConstant.SELECT_FROM_USER);
        sql = buildSqlCommon(params, sql);
        return sql.toString();
    }


    private StringBuilder buildSqlCommon(Map<String, String> params, StringBuilder sql) {
        String email = params.get("email");
        String status = params.get("enable_query"); // 1 or 0
        String fullName = params.get("fullName");
        String roleId = params.get("role_id"); // 1, 2, 3.

        if (email != null) {
            if (!email.isEmpty()) {
                sql.append(" and u.email like '%" + email + "%'");
            }
        }

        if (status != null) {
            if (!status.isEmpty()) {
                sql.append(" and u.is_enabled = " + status);
            }
        }

        if (fullName != null) {
            if (!fullName.isEmpty()) {
                sql.append(" and u.full_name like '%" + fullName + "%'");
            }
        }

        if (roleId != null) {
            if (!roleId.isEmpty()) {
                sql.append(String.format(" and role.id = %s",roleId));
            }
        }
        return sql;
    }
}
