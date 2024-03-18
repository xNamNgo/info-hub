package com.info_hub.constant;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CommonSQLConstant {
    private String tableName;

    public String getSelectTable() {
        return "SELECT a.* FROM " + this.tableName + " AS a WHERE 1 = 1";
    }

    public StringBuilder setWhereClause(Map<String, String> params, StringBuilder sql) {
        List<String> whereClause = new ArrayList<>();
        for (String name : params.keySet()) {
            String value = params.get(name);
            if (value == null || name.endsWith("_ids")) continue;
            if(name.contains("limit") || name.contains("page")) continue;

            // trường hợp so sánh bằng (id, truyền tiền tố s)
            // filter => contains (prefix is f_)
            // search => equals (prefix is s_)

            // field name là sau khi xử lý tiền tố
            String fieldName = name.substring(2);

            // field value là value của field trong params (không xử lý tiền tố)
            String fieldValue = params.get(name).trim();
            if (fieldValue.isEmpty()) continue;
            if (name.equals("f_date_from")) {
                whereClause.add(String.format("a.%s >= '%s'", "created_date", fieldValue));
            } else if (name.equals("f_date_to")) {
                whereClause.add(String.format("a.%s < '%s'", "created_date", fieldValue));
            } else if ((name.endsWith("_id") && name.startsWith("f_"))
                    || (name.endsWith("_by") && name.startsWith("f_"))) {

                whereClause.add(String.format("a.%s = %s", fieldName, fieldValue));
            } else if(name.startsWith("s_")) {
                whereClause.add(String.format("a.%s = '%s'", fieldName, fieldValue));
            }
            else if (name.startsWith("f_")) {
                whereClause.add("a." + fieldName + " LIKE '%" + fieldValue + "%'");
            }
        }
        if (!whereClause.isEmpty()) {
            sql.append(" AND ")
                    .append(String.join(" AND ", whereClause));
        }
        return sql;
    }

}
