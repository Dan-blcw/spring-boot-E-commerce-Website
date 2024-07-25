package com.dan_michael.example.demo.model.dto.TestMomo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundRequest {
    private String partnerTransId;
    private String momoTransId;
    private String requestId;
    private long amount;
    private String partnerRefId;
}