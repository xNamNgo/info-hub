package com.info_hub.repositories.user;

import com.info_hub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends UserRepositoryCustom, JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    @Query("select u from User u where u.role.code in (:roleCodes)")
    List<User> findByRole_Code(String[] roleCodes);

    long countByRole_Code(String code);

}
