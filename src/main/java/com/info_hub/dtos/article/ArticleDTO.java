package com.info_hub.dtos.article;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleDTO {
    @NotEmpty
    @Size(min= 20, message = "Title should have at least 20 characters.")
    private String title;

    @NotEmpty
    @Size(min= 30, message = "Description should have at least 30 characters.")
    private String description;

    // FE call api: upload image, then pass id from that response into this field.
    @NotNull
    private Integer thumbnailId;

    @NotEmpty
    @Size(min= 50, message = "Description should have at least 100 characters.")
    private String content; // ckEditor

    @NotNull
    private Integer categoryId;

    // [1,2,3,4]
    @NotNull
    private List<Integer> tagIds;

}
