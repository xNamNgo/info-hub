package com.info_hub.dtos.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllMyCommentResponse {
    private Date createdDate;
    private String text;
    private Integer articleId;
    @JsonProperty("categoryCode")
    private String articleCategoryCode;
    @JsonProperty("cate_parent_code")
    private String articleCategoryParentCategoryCode;


}
