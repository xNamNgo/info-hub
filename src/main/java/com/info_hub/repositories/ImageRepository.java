package com.info_hub.repositories;

import com.info_hub.models.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Integer> {
    Image findByUsers_Id(Integer id);
}
