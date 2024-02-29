package com.info_hub.dtos.auth;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {

    @Min(value = 8,message = "Password must more than 8 characters")
    private String password;
}
