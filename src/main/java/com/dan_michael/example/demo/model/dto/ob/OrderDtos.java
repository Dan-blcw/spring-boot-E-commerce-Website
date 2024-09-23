package com.dan_michael.example.demo.model.dto.ob;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class OrderDtos {
    @JsonProperty("user_id")
    private Integer userId;
    private List<ItemDetailDto> orderDetails;
    private String address;
    private String skuDiscount;
    private String phoneNumber;
    private String emailAddress;

    private String paymentMethods;
    private Integer paymentStatus;

    private Float shippingFee;

    private String percentDiscountSku;
    private Integer percentDiscount;
    private String orderStatus;
}
