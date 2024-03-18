package com.info_hub.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponse {
    private Integer id;
    private String email;
    private String fullName;
    private String role;
    private LocalDateTime createdAt;
}
