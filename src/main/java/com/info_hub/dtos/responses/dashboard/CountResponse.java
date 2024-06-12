package com.info_hub.dtos.responses.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CountResponse {
    @JsonProperty("approved_totals")
    private int approvedTotals;

    @JsonProperty("pending_totals")
    private int pendingTotals;

    @JsonProperty("rejected_totals")
    private int rejectedTotals;

}
