package com.info_hub.controllers;

import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.ReviewDTO;
import com.info_hub.dtos.article.ArticleDTO;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.article.ArticleDetailResponse;
import com.info_hub.dtos.responses.article.ArticleListResponse;
import com.info_hub.dtos.responses.article.UserResponse;
import com.info_hub.services.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse<ArticleListResponse> getAll(
            @RequestParam(required = false) Map<String, String> params) {
        SimpleResponse<ArticleListResponse> response = articleService.getAllArticles(params);
        return response;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ArticleDetailResponse> getById(@PathVariable Integer id) {
        ArticleDetailResponse response = articleService.getArticleById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * The default status when creating an article is "PENDING."
     * This means the article will only display if the status is "APPROVED."
     * When creating an article, the next step is that it needs to be approved
     * before it can be displayed on the list of articles.
     */
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR','JOURNALIST')")
    @PostMapping
    public ResponseEntity<ResponseMessage> create(@Valid @RequestBody ArticleDTO body) {
        ResponseMessage response = articleService.createArticle(body);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * When collaborator, admin approve - that means article will change status
     * from "Pending" to "Approved"
     * otherwise "Rejected"
     *
     * @param status: "REJECTED" or "APPROVED"
     * @return message
     */
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR')")
    @PatchMapping("/review/{id}")
    public ResponseEntity<ResponseMessage> reviewrArticle(
            @PathVariable("id") Integer articleId,
            @RequestBody ReviewDTO status) {
        ResponseMessage response = articleService.reviewArticle(articleId, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR','JOURNALIST')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> update(@PathVariable Integer id,
                                                  @RequestBody ArticleDTO request) {
        ResponseMessage response = articleService.updateArticle(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list-reviewers")
    public ResponseEntity<List<UserResponse>> listReviewers() {
        List<UserResponse> response = articleService.getListReviewers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list-authors")
    public ResponseEntity<List<UserResponse>> listAuthors() {
        List<UserResponse> response = articleService.getListAuthors();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * save or unsaved article
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/save/{articleId}")
    public ResponseEntity<ResponseMessage> saveArticle(@PathVariable Integer articleId) {
        ResponseMessage response = articleService.saveArticle(articleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
















}
