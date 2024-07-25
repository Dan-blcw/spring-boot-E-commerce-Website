package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @JsonProperty("content")
    private String content;

    @JsonProperty("rating")
    private Float rating;

    @JsonProperty("productQuality")
    private String productQuality;

    @JsonProperty("username")
    private String username;
}
