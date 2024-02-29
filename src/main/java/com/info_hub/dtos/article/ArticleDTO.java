package com.info_hub.dtos.article;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ArticleDTO {
    @NotEmpty
    @Size(min= 20, message = "Title should have at least 20 characters.")
    private String title;

    @NotEmpty
    @Size(min= 100, message = "Description should have at least 100 characters.")
    private String description;
    private String content; // ckEditor
    private Integer categoryId;
    private List<Integer> tagIds;
}
