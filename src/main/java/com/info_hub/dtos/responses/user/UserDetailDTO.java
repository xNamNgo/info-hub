package com.info_hub.dtos.responses.user;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserDetailDTO {
    private Integer id;
    private String fullName;
    private String email;
    private Integer roleId;
    private String roleName;
    private boolean isEnabled;
}
