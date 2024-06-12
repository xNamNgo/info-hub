package com.info_hub.dtos.responses.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleSaved {
    @JsonProperty("is_saved")
    private boolean isSaved;

}
