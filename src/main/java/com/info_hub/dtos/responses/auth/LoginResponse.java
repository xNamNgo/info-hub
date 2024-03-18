package com.info_hub.dtos.responses.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private Integer id;
    private String accessToken;
    private String refreshToken;
    private String email;
    private String fullName;
    private String role;
    private String image;
    private String message;
}
