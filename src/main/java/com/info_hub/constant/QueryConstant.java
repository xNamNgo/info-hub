package com.info_hub.constant;

public class QueryConstant {
    public static final String WHERE_ONE_EQUAL_ONE = " WHERE 1 = 1";
    public static final String ORDER_BY_DATE_ARTICLE_DESC = " ORDER BY a.created_date DESC";
    public static final String LIMIT = " LIMIT ";
    public static final String OFFSET = " OFFSET ";
    public static final String SELECT_FROM_ARTICLE = "SELECT a.* FROM article AS a";
    public static final String SELECT_FROM_USER = "SELECT u.* FROM user AS u" +
            " JOIN role on u.role_id = role.id\n" +
            " WHERE role.code NOT IN ('ROLE_ADMIN')";
}
