package com.info_hub.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Comment extends BaseEntity{
    @Column(columnDefinition = "integer default 0")
    private int userId;
    @Column(columnDefinition = "integer default 0")
    private int parentId;
    @Column(columnDefinition = "integer default 0")
    private int articleId;

    @Column(nullable = false)
    private String text;
}
