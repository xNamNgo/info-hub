package com.info_hub.dtos.responses.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleComments {
    private String userFullName;
    private String userEmail;
    private String userImageUrl;
    private String text;
    private Date createdDate;
}
