package com.info_hub.repositories;

import com.info_hub.models.Token;
import com.info_hub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
//    Optional<Token> findByToken(String refreshToken);
    List<Token> findByUser(User user);
    Optional<Token> findByRefreshToken(String refreshToken);

    @Query(value = "delete from token where user_id = ?1", nativeQuery = true)
    @Modifying
    void deleteByUserId(Integer id);

    Token findByAccessToken(String accessToken);
}
