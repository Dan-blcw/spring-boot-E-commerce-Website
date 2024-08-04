package com.dan_michael.example.demo.model.dto.ob;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartDetailDtos {
    @JsonProperty("user_id")
    private Integer userId;
    private ItemDetailDto cart_item;
}
