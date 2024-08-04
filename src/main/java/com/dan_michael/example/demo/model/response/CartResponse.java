package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.dto.ob.ItemDetailDto;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.SubEn.QuantityDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Integer cart_id;
    private Integer user_id;
    private Float totalPayment;
    private Integer totalQuantity;
    private List<SubCart_OrderResponse> cart_items;
}