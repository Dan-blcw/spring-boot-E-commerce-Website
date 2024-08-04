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

    @JsonProperty("itemDetail_id")
    private Integer itemDetail_id;
    private String name;
    private String image;
    @JsonProperty("quantity")
    private Integer quantity;
    private String size;
    private String color;

    private Float unitPrice;

    private Float totalPrice;
}
