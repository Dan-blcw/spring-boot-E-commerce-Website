//package com.dan_michael.example.demo.service.Payment;
//
//import com.dan_michael.example.demo.model.dto.*;
//import com.dan_michael.example.demo.model.dto.TestMomo.POSPaymentRequest;
//import com.mservice.pay.models.POSPayResponse;
//import com.mservice.pay.models.TransactionQueryResponse;
//import com.mservice.pay.models.TransactionRefundResponse;
//import com.mservice.pay.processor.notallinone.POSPay;
//import com.mservice.pay.processor.notallinone.TransactionQuery;
//import com.mservice.pay.processor.notallinone.TransactionRefund;
//import com.mservice.shared.constants.Parameter;
////import com.mservice.shared.constants.ProcessType;
//import com.mservice.shared.constants.RequestType;
//import com.mservice.shared.sharedmodels.Environment;
//import com.mservice.shared.sharedmodels.PartnerInfo;
//import org.springframework.stereotype.Service;
//package com.mservice.shared.sharedmodels;
//@Service
//public class PaymentService {
//
//    private final PartnerInfo devInfo = new PartnerInfo("MOMOLRJZ20181206", "mTCKt9W3eU1m39TW", "KqBEecvaJf1nULnhPF5htpG3AMtDIOlD");
//    private final String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAlsL+G4UyFO0UQsQ4cAXuGYcn38d67PluKmeJqS2RcAqnNUFJjQieI5DSCyHVgAmPpUfDZ3CiSw+5NCfnjgChd/p4fq3bnGqSIw2JP78UROQDJYqfAc+WLvT29IgCH4O+P9+lOLUj2EWf8aqHxBwC1YPxtxK+8M+LKdVMAvZd3lXE3MBg9wTDYEcCNODXkNma/SfIKJCvmVWdKeKXd6IwW7yA0oTjdguAeqP8+O8jLjxJH57otRh63iX945vqAX2YAm9qzVoiDcWpv+UubRmbZ9l0moQwkdsDyCtCYPUtcW6kkdxuhlq8rg8RAVcinsz/843CBYHtqaUaAFQU1TO5EXiXT87zx/Oj2Bf4OC+iAJL/UQ4ASeL1vMoOfDSpSE8EnqKPyP+rM/H7oUaJrIin8KkrxmDLGQWKhNcTFO6UNPv3Hh13tEBv0GRy2vktL8+CWhrYHouXF2XwpS8uR/gH/Vl+5HT/HsTv/13gjSoGBQcdfyck9ZyHh5oBrQTds52C2vabCqWCEafRMbpj7lSrDWS2Df+XznR/hGkgewSdSZ/M0VK/DLadJ3x1Yhblv1HVw3jA3xzY1/zlNOZReLuvW6/kdRwJV/Zj5bd9eLJnz9jDPUcB0hAO+JuJYfTVuhZG9Beo1JbQ9+cFx+92ELn/yHDMod6rfrfBjikU9Gkxor0CAwEAAQ==";
//
//    public POSPayResponse processPOSPayment(POSPaymentRequest request) {
//        try {
//            POSPayResponse dev = POSPay.process(
//                    Environment.selectEnv("dev", Environment.ProcessType.PAY_POS),
//                    request.getPartnerRefId(),
//                    request.getAmount(),
//                    request.getPartnerName(),
//                    request.getStoreId(),
//                    request.getStoreName(),
//                    publicKey,
//                    request.getCustomerNumber(),
//                    request.getPaymentCode(),
//                    Parameter.VERSION,
//                    Parameter.APP_PAY_TYPE
//            );
//            return dev;
//        } catch (Exception e) {
//            throw new RuntimeException("Error processing POS payment", e);
//        }
//    }
//
//    public TransactionQueryResponse queryTransaction(String partnerTransId, String requestId) {
//        try {
//            return TransactionQuery.process(
//                Environment.selectEnv(Environment.ProcessType.DEV_ENV, Environment.ProcessType.PAY_QUERY_STATUS),
//                partnerTransId,
//                requestId,
//                publicKey,
//                "",
//                Parameter.VERSION
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Error querying transaction", e);
//        }
//    }
//
//    public TransactionRefundResponse refundTransaction(RefundRequest request) {
//        try {
//            return TransactionRefund.process(
//                Environment.selectEnv("dev", ProcessType.PAY_QUERY_STATUS),
//                request.getPartnerTransId(),
//                request.getMomoTransId(),
//                publicKey,
//                request.getRequestId(),
//                request.getAmount(),
//                "",
//                request.getPartnerRefId(),
//                Parameter.VERSION
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Error processing refund", e);
//        }
//    }
//}
