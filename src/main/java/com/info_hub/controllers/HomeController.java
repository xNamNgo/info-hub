package com.info_hub.controllers;

import com.info_hub.dtos.home.ArticleResponse;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.home.ArticleComments;
import com.info_hub.dtos.responses.home.DetailArticleHome;
import com.info_hub.services.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/home")
public class HomeController {
    private final HomePageService homeService;

    @GetMapping("/articles")
    public ResponseEntity<SimpleResponse<ArticleResponse>> fetchHomeData(@RequestParam(required = false) Map<String, String> params) {
        return ResponseEntity.ok(homeService.fetchHomeData(params));
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<DetailArticleHome> getDetailArticle(@PathVariable Integer articleId) {
        return ResponseEntity.ok(homeService.getDetailArticle(articleId));
    }

    @GetMapping("/article/{articleId}/comments")
    public ResponseEntity<SimpleResponse<ArticleComments>> getAllCommentFromArticle(@PathVariable Integer articleId,
                                                                                    @RequestParam(required = false) Map<String, String> params) {
        return ResponseEntity.ok(homeService.getAllCommentFromArticle(articleId,params));
    }

}
