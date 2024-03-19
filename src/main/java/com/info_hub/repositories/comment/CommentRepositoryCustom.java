package com.info_hub.repositories.comment;

import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.models.Comment;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CommentRepositoryCustom {
    SimpleResponse<Comment> findByCondition(Map<String, String> params, Pageable pageable);
}
