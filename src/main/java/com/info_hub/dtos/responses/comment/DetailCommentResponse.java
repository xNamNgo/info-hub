package com.info_hub.dtos.responses.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailCommentResponse {
    @JsonProperty("commentId")
    private Integer id;
    private String userEmail;
    private String articleTitle;
    private String text;
}
