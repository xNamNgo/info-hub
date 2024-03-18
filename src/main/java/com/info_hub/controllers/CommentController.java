package com.info_hub.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/comments")
@PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR')")
public class CommentController {

//    public CommentController(CommentService service) {
//        super(service);
//    }
//
//    @Override
//    @PostMapping
//    public ResponseEntity<Comment> create(@RequestBody Comment body) {
//        return super.create(body);
//    }
//
//    @Override
//    @PatchMapping
//    public ResponseEntity<Comment> update(@RequestBody Comment body) {
//        return super.update(body);
//    }
//
//    @Override
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteById(@PathVariable int id) {
//        return super.deleteById(id);
//    }
}
