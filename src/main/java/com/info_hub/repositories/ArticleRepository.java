package com.info_hub.repositories;

import com.info_hub.models.Article;
import com.info_hub.models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends BaseRepository<Article> {

    @Query("select t from Tag t where t.id in (:tag_ids)")
    List<Tag> getTags(@Param("tag_ids") int[] tagIds);
}
