package com.info_hub.dtos.responses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.info_hub.enums.Status;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogResponse {
    @JsonProperty("article_id")
    private Integer id;

    @JsonProperty("author_name")
    private String authorFullname;

    @JsonProperty("author_role")
    private String authorRoleCode;

    @JsonProperty("article_status")
    private Status status;

    @JsonProperty("created_date")
    private Date createdDate;

}
