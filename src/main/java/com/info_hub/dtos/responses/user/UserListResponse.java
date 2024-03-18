package com.info_hub.dtos.responses.user;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserListResponse {
    private Integer id;
    private String email;
    private String fullName;
    private String roleName;
    private boolean isEnable;
}
