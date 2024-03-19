package com.info_hub.controllers;

import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.ReviewDTO;
import com.info_hub.dtos.comment.CommentDTO;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.comment.DetailCommentResponse;
import com.info_hub.dtos.responses.comment.ListCommentResponse;
import com.info_hub.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comments")
public class CommentController {
    private final CommentService commentService;

    // get all comments from all articles
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR')")
    @GetMapping("/list-comments")
    public ResponseEntity<SimpleResponse<ListCommentResponse>> listComment(
            @RequestParam(required = false) Map<String, String> params) {
        return ResponseEntity.ok(commentService.getListComment(params));
    }

    // detail comment by comment id
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR')")
    @GetMapping("/detail/{commentId}")
    public ResponseEntity<DetailCommentResponse> detailComment(
            @PathVariable Integer commentId) {
        return ResponseEntity.ok(commentService.getDetailComment(commentId));
    }

    // review comment by comment id
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR')")
    @PatchMapping("/review/{commentId}")
    public ResponseEntity<ResponseMessage> reviewComment(
            @PathVariable Integer commentId, @RequestBody ReviewDTO req) {
        return ResponseEntity.ok(commentService.reviewComment(commentId, req.getStatus()));
    }

    // call this api when user comment in article id
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/request-comment/{articleId}")
    public ResponseEntity<ResponseMessage> requestComment(
            @PathVariable Integer articleId,
            @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.requestComment(articleId, commentDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ResponseMessage> deleteComment(@PathVariable Integer commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }

}
