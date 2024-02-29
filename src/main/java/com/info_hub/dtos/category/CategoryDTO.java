package com.info_hub.dtos.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryDTO {
    private String name;
    private String code;
    private Integer parentId;
}
