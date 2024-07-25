package com.dan_michael.example.demo.model.dto.TestMomo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class POSPaymentRequest {
    private String partnerRefId;
    private long amount;
    private String partnerName;
    private String storeId;
    private String storeName;
    private String customerNumber;
    private String paymentCode;
}
