package com.info_hub.dtos.article;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleSearchDTO {
    @JsonProperty("f_title")
    private String title;

    @JsonProperty("f_authorname")
    private String authorFullname;

    @JsonProperty("f_reviewername")
    private String reviewerFullname;
}
