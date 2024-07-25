package com.dan_michael.example.demo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessageDtos {
    @JsonProperty("status")
    Integer status;

    @JsonProperty("message")
    String message;
}
