package com.dan_michael.example.demo.model.dto.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VNPayResponseDtos {
    public String code;
    public String message;
    public String paymentUrl;
}