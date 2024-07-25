package com.dan_michael.example.demo.model.dto.global;

import lombok.Builder;

public abstract class PaymentVNPayDTO {
    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;

        public VNPayResponse(String code, String message, String paymentUrl) {
            this.code = code;
            this.message = message;
            this.paymentUrl = paymentUrl;
        }

    }
}
