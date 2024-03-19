package com.info_hub.dtos;

import com.info_hub.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Status status; // APPROVED, PENDING, REJECTED
    private String message; // if rejected
}
