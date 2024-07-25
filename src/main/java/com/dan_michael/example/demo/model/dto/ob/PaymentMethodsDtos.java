package com.dan_michael.example.demo.model.dto.ob;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class PaymentMethodsDtos {
    public String paymentMethodsName;
    public String description;
    public Date createdDate;
    public Integer status;
    private byte[] image;
}
