package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@Builder
public class CartDtos {

    @JsonProperty("cart_id")
    private Integer cart_id;
    @JsonProperty("user_id")
    private Integer userId;
    private ItemDetailDto cart_item;
}
