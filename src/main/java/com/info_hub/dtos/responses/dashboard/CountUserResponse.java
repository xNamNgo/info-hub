package com.info_hub.dtos.responses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CountUserResponse {


    //    private int
    @JsonProperty("user_totals")
    private int userTotals; // người dùng

    @JsonProperty("collaborator_totals")
    private int collaboratorTotals; // cộng tác viên

    @JsonProperty("journalist_totals")
    private int journalistTotals; // nhà báo

    //JOURNALIST
}
