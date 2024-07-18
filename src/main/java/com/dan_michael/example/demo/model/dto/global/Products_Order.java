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
public class Products_Order {

    @JsonProperty("product_id")
    private Integer id;

    @JsonProperty("quantity")
    private Integer quantity;
}
