package com.dan_michael.example.demo.model.dto.global;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSenderDtos {
    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;

    @JsonProperty("discountCode")
    private String discountCode;

}
