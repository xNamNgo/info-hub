package com.info_hub.dtos.responses.dashboard;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DataResponse<T> {
    private List<T> data;
}
