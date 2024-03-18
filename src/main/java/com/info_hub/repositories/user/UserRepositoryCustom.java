package com.info_hub.repositories.user;

import com.info_hub.models.User;
import com.info_hub.dtos.responses.SimpleResponse;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserRepositoryCustom {
    SimpleResponse<User> findByCondition(Map<String, String> params, Pageable pageable);
}
