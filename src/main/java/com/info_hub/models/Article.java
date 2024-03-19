package com.info_hub.models;

import com.info_hub.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "thumbnail_id")
    private Image thumbnail;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String rejectedMessage;

    // reviewer
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    // author
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "article_tag",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "article_saved",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users;

    public void addAllTag(List<Tag> tag) {
        if (this.tags == null) {
            this.tags = new HashSet<>();
        } else {
            this.tags.clear();
        }
        this.tags.addAll(tag);
    }

    public void addCategory(Category category) {
        this.category = category;
    }

    public void addSavedArticle(User loggedInUser) {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
        this.users.add(loggedInUser);
    }

    public void removeSavedArticle(User loggedInUser) {
        this.users.remove(loggedInUser);
    }

    public boolean isSavedArticle(User loggedInUser) {
        // check did this article has saved by this user ?
        return this.users.contains(loggedInUser);
    }

}
