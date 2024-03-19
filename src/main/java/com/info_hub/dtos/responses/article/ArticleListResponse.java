package com.info_hub.dtos.responses.article;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListResponse {
    private Integer id;
    private String title;
    private Date updatedDate;
    private String categoryName;
    private String authorFullname;
    private String reviewerFullname;
    private String status;

    // load to client
    private String description;
    private String thumbnailUrl;
    private Integer categoryId;


}
