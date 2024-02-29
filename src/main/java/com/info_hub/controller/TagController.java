package com.info_hub.controller;

import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.category.CategoryDTO;
import com.info_hub.dtos.tag.TagDTO;
import com.info_hub.responses.ListResponse;
import com.info_hub.responses.category.CategoryResponse;
import com.info_hub.responses.tag.TagResponse;
import com.info_hub.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<ListResponse<TagResponse>> getAll(
            @RequestParam(required = false) Map<String, String> params) {
        ListResponse<TagResponse> response = tagService.getAllTags(params);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getById(@PathVariable Integer id) {
        TagResponse response = tagService.getTagById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR')")
    @PostMapping
    public ResponseEntity<ResponseMessage> create(@RequestBody TagDTO request) {
        ResponseMessage response = tagService.createTag(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseMessage> update(@PathVariable Integer id,
                                                  @RequestBody TagDTO request) {
        ResponseMessage response = tagService.updateTag(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable int id) {
        ResponseMessage response = tagService.deleteTag(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
