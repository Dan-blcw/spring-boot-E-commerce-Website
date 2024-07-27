package com.dan_michael.example.demo.model.dto.ob;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailDto {

    private Integer id;

    @JsonProperty("product_id")
    private Integer product_id;

    @JsonProperty("quantity")
    private Integer quantity;
    private String size;
    private String colors;

    private Float subTotal;

    private Float total;
}
