package com.info_hub.dtos.home;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// HomeController
public class ArticleResponse {
    private Integer id;
    private String title;
    private String description;
    private String thumbnailUrl;

    private String categoryCode;

    @JsonProperty("cate_parent_code")
    private String categoryParentCategoryCode;

    @JsonProperty("cate_parent_id")
    private Integer categoryParentCategoryId;
}
