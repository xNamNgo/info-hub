package com.info_hub.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDTO {
    @NotBlank(message = "Refresh token cannot be empty")
    private String refreshToken;
}
