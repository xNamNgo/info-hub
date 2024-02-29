package com.info_hub.controller;

import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.article.ArticleDTO;
import com.info_hub.responses.ListResponse;
import com.info_hub.responses.article.ArticleListResponse;
import com.info_hub.responses.article.ImgResponse;
import com.info_hub.services.ArticleService;
import com.info_hub.services.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ImageService imageService; // upload image.

    // pending
    @GetMapping
    public ResponseEntity<ListResponse<ArticleListResponse>> getAll(
            @RequestParam(required = false) Map<String, String> params) {
        ListResponse<ArticleListResponse> response = articleService.getAllArticles(params);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // pending
//    @GetMapping("/{id}")
//    public ResponseEntity<ArticleDetailResponse> getById(@PathVariable Integer id) {
//        ArticleDetailResponse response = articleService.getArticleById(id);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ArticleDTO body) {
        ResponseMessage response = articleService.createArticle(body);
        return new ResponseEntity<>("Test", HttpStatus.CREATED);
    }

    /**
     * Upload image.
     * @param img < 5MB
     * @return url of image.
     */
    @PostMapping(value = "/uploads")
    public ResponseEntity<ImgResponse> uploadImages(@RequestParam("file") MultipartFile img) {
        ImgResponse urlImage = imageService.createImageArticle(img);
        return new ResponseEntity<>(urlImage, HttpStatus.CREATED);
    }
    

}
