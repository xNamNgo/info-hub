package com.info_hub.responses.auth;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LoginErrorResponse {
    private String message;
}
