package com.dan_michael.example.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCart_OrderResponse {
    private Integer itemDetail_id;
    private Integer product_id;
    private Integer quantity;
    private String color;
    private String size;
    private Float subTotal;
}
