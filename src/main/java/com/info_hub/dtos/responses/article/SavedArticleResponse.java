package com.info_hub.dtos.responses.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SavedArticleResponse {
    private Integer id;
    private String title;
    private String thumbnailUrl;
    private String categoryName;
    private String categoryCode;
    @JsonProperty("cate_parent_code")
    private String categoryParentCategoryCode;

    private Date createdDate;
}
