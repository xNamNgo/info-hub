package com.info_hub.responses.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private Integer id;
    private String name;
    private String code;
    private Integer parentId;
    private String parentName;
}
