package com.info_hub.dtos.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private Integer id;
    private String fullName;
    private String email;
    @JsonProperty("imageUrl")
    private String image;


}
