package com.info_hub.repositories;

import com.info_hub.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends BaseRepository<Tag> {
    boolean existsByName(String name);
    boolean existsByCode(String code);

    @Query("SELECT t FROM Tag t WHERE t.name LIKE %?1% or t.code LIKE %?1%")
    Page<Tag> search(String keyword, Pageable pageable);

}
