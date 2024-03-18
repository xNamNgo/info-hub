package com.info_hub.dtos.responses.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ImgResponse {
    private String url;
    private String fileSize;
    private String fileName;
    private String mime; // like image/jpeg; application/pdf; text/plain

}
