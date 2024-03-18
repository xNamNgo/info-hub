package com.info_hub.dtos.responses.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ArticleListResponse {
    private Integer id;
    private String title;
    private Date updatedDate;;
    private String categoryName;
    private String authorFullname;
    private String reviewerFullname;
    private String status;
}
