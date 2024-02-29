package com.info_hub.repositories;

import com.info_hub.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends BaseRepository<Category> {
    boolean existsByParentCategory_Id(Integer id);

    @Query("select c from Category c where c.parentCategory.id = :id")
    List<Category> findSubCategory(long id);

    boolean existsByCode(String code);
    boolean existsByName(String name);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1% or c.code LIKE %?1%")
    Page<Category> search(String keyword, Pageable pageable);
}
