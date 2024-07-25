package com.dan_michael.example.demo.model.response;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePayPal {
    PayerInfo payerInfo;
    Transaction transaction;
    ShippingAddress shippingAddress;

    String url;
}
