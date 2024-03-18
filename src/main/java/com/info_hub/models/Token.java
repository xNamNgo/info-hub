package com.info_hub.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private LocalDateTime accessExpirationDate;
    private LocalDateTime refreshExpirationDate;
    private boolean isRevoked;
    private boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
