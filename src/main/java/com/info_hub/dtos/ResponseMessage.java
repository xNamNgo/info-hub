package com.info_hub.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseMessage {
    private String message;

    public static ResponseMessage success() {
        return new ResponseMessage("Successfully!");
    }
}
