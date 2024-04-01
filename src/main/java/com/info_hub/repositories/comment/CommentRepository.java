package com.info_hub.repositories.comment;

import com.info_hub.enums.Status;
import com.info_hub.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends CommentRepositoryCustom, JpaRepository<Comment,Integer> {
    List<Comment> findByUser_Id(Integer id);

    Page<Comment> findByStatusAndArticle_Id(Status status, Integer id,Pageable pageable);

}
