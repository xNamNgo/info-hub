package com.info_hub.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "article_monitors")
public class ArticleMonitorsEntity extends BaseEntity{
    @Column(columnDefinition = "integer default 0")
    private int userId;

    private String statusType;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
