package com.info_hub.responses.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String fullName;
    private String role;
    private String message;
}
