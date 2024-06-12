package com.info_hub.dtos.responses.home;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.info_hub.models.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailArticleHome {
    @JsonProperty("articleId")
    private Integer id;
    private String authorFullName;
    private String authorEmail;
    private String authorImageUrl;
    private Date createdDate;
    private String title;
    private String description;
    private String content;
    private Set<Tag> tags;

    // category
    @JsonProperty("subCatId")
    private Integer categoryId;
    @JsonProperty("subCatName")
    private String categoryName;
    @JsonProperty("subCatCode")
    private String categoryCode;
    @JsonProperty("pCatId")
    private Integer categoryParentCategoryId;
    @JsonProperty("pCatName")
    private String categoryParentCategoryName;
    @JsonProperty("pCatCode")
    private String categoryParentCategoryCode;



}
