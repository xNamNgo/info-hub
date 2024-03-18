package com.info_hub.dtos.article;

import com.info_hub.enums.ArticleStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveArticleDTO {
    private ArticleStatus status; // APPROVED,
    private String message; // if rejected
}
