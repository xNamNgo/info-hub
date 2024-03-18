package com.info_hub.dtos.responses.article;

import com.info_hub.dtos.responses.category.CategoryNodeResponse;
import com.info_hub.dtos.responses.tag.TagResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDetailResponse {
    private Integer articleId;
    private String authorName;
    private String title;
    private String description;
    private String thumbnailUrl;
    private List<TagResponse> tags;
    private CategoryNodeResponse category;
    private String content;
    private String status;
    private String message; // if rejected
}
