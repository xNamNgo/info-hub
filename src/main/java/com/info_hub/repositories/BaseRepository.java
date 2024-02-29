package com.info_hub.repositories;

import com.info_hub.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T,Integer> {

//    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
    @Query("Select t from #{#entityName} t")
    Page<T> search(String keyword, Pageable pageable);
}
