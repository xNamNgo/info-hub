package com.info_hub.dtos.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleResponse<T> {
    public List<T> data;
    public int limit;
    public int page;
    public int totalItems;
    public int totalPage;
}
