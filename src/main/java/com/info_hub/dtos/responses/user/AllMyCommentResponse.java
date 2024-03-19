package com.info_hub.dtos.responses.user;

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
}
