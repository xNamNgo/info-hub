package com.info_hub.dtos.responses.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.info_hub.enums.Status;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListCommentResponse {
    @JsonProperty("commentId")
    private Integer id; // comment id
    private String articleTitle;
    private String userEmail;
    private String text;
    private Date createdDate;
    private Status status;
    private String reviewerFullName;
}
