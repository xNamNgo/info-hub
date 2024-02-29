package com.info_hub.dtos.auth;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expirationAccessToken;
    private LocalDateTime expirationRefreshToken;
}
