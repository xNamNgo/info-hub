package com.info_hub.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListResponse<T> {
    public List<T> data;
    public int limit;
    public int page;
    public int totalItems;
    public int totalPage;
}
