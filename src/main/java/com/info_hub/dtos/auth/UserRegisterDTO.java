package com.info_hub.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegisterDTO {
    @NotNull(message = "Email is required")
    @Email(message = "Please enter a valid email ( ex: abc123@gmail.com )")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8,message = "Password must more than 8 characters")
    private String password;

    @NotBlank(message = "Display name cannot be blank.")
    @Size(min = 2, message = "Display name must more than 2 characters")
    private String fullName;
}
