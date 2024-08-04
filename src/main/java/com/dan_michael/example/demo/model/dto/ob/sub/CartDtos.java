package com.dan_michael.example.demo.model.dto.ob.sub;

import com.dan_michael.example.demo.model.dto.ob.ItemDetailDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartDtos {
    @JsonProperty("user_id")
    private Integer userId;
    private List<ItemDetailDto> cart_items;
}
