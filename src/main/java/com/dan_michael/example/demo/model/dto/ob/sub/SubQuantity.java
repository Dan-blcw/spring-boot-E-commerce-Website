package com.dan_michael.example.demo.model.dto.ob.sub;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubQuantity {
    private String color;
    private String size;
    private Integer quantity;
}
