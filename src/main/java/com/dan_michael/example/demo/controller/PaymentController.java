package com.dan_michael.example.demo.controller;

//import com.dan_michael.example.demo.exception.*;
import com.dan_michael.example.demo.model.dto.ob.OrderDtos;
import com.dan_michael.example.demo.model.response.ResponsePayPal;
import com.dan_michael.example.demo.service.Payment.PaymentPaypalService;
import com.dan_michael.example.demo.model.dto.global.PaymentVNPayDTO;
import com.dan_michael.example.demo.model.response.ResponseObject;
import com.dan_michael.example.demo.service.Payment.PaymentVNPayService;
import com.dan_michael.example.demo.service.Payment.PaymentMethodsService;
import com.dan_michael.example.demo.util.Constants;
import com.mservice.allinone.models.*;
import com.mservice.allinone.processor.allinone.*;
//import com.mservice.shared.sharedmodels.Environment;
//import com.mservice.shared.sharedmodels.PartnerInfo;
import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.PartnerInfo;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import com.momo.partner.api.response.AppPayResponse;
//import com.mservice.shared.constants.RequestType;


@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentPaypalService paypalService;

    private final PaymentVNPayService vnPayService;

    private final PaymentMethodsService paymentMethodsService;
    @GetMapping(value = "/payments-methods-name")
    public List<String> listPaymentMethods(){
        return paymentMethodsService.listPaymentMethodsName();
    }
    @GetMapping("/vn-pay")
    public ResponseObject<PaymentVNPayDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(
                HttpStatus.OK,
                Constants.VnPayStatus_Success,
                vnPayService.createVnPayPayment(request));
    }
    @GetMapping("/vn-pay-callback")
    public ResponseObject<PaymentVNPayDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request, @RequestParam String vnp_ResponseCode) {
    //http://localhost:8080/api/v1/payment/vn-pay-callback?vnp_Amount=27000000&vnp_BankCode=NCB&vnp_BankTranNo=VNP14530123&vnp_CardType=ATM&vnp_OrderInfo=Thanh+toan+don+hang%3A52809020&vnp_PayDate=20240725003857&vnp_ResponseCode=00&vnp_TmnCode=58X4B4HP&vnp_TransactionNo=14530123&vnp_TransactionStatus=00&vnp_TxnRef=49871732&vnp_SecureHash=2eab4b879ba583e1a8687583416da734437828061225a9402a453c5b6f4ab20c8e45e2d8f0bf2dded13ffeb4bf6f97590f5a26e00af5549798c3d6d77899eb7f
    //        String status = vnp_ResponseCode);
        if (vnp_ResponseCode.equals("00")) {
            return new ResponseObject<>(
                    HttpStatus.OK,
                    Constants.VnPayStatus_Success,
                    new PaymentVNPayDTO.VNPayResponse(
                            Constants.VnPay_Code_00,
                            Constants.VnPayStatus_Success,
                            ""
                    )
            );
        } else {
            return new ResponseObject<>(
                    HttpStatus.BAD_REQUEST,
                    Constants.VnPayStatus_Fail,
                    null
            );
        }
    }
    //(vnp_TmnCode): CGXZLS0Z
    //(vnp_HashSecret): XNBCJFAKAZQSGTARRLGCHVZWCIOIGSHN
    //Ngân hàng: NCB
    //Số thẻ: 9704198526191432198
    //Tên chủ thẻ:NGUYEN VAN A
    //Ngày phát hành:07/15
    //Mật khẩu OTP:123456

//-----------------------------------------------------------------------------------------------------
    @PostMapping("/paypal-payment")
    public String doPost(@RequestBody OrderDtos orderDtos) throws PayPalRESTException {
        String approvalLink = paypalService.authorizePayment(orderDtos);
        return approvalLink;
    }
    //https://developer.paypal.com/tools/sandbox/card-testing/
    //card number: 4032037784288207
    //Expiry Date: 11/2025
    //CVC code: 070
    //Mã Bưu điện: 800000
    @GetMapping("/paypal-review-payment")
    public ResponseEntity<?> doGet(@RequestParam (required = false)String paymentId,
                        @RequestParam (required = false)String PayerID) throws PayPalRESTException {
        try{
            Payment payment = paypalService.getPaymentDetails(paymentId);
            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);
            ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
    //http://localhost:8080/api/v1/payment/review_payment?paymentId=PAYID-M2QR5GQ3JM33889FY212470G&token=EC-91J08678NY522190J&PayerID=BKS5X4VG9HWPN
            String url = "review_payment?paymentId=" + paymentId +"&PayerID=" +PayerID;
            return ResponseEntity.ok(
                    ResponsePayPal.builder()
                            .payerInfo(payerInfo)
                            .transaction(transaction)
                            .shippingAddress(shippingAddress)
                            .url(url).build()
            );
        }catch (PayPalRESTException e){
            e.printStackTrace();
        }
        return null;
    }
    //Các Thống tin sẽ lấy
    //transaction.description
    //transaction.amount.details.subtotal
    //transaction.amount.details.shipping
    //transaction.amount.details.tax
    //transaction.amount.total

    //payer.email
    //payer.firstname
    //payer.lastname

    //shippingAddress.recipientName
    //shippingAddress.line1
    //shippingAddress.city
    //shippingAddress.state
    //shippingAddress.countryCode
    //shippingAddress.póstalCode

//-----------------------------------------------------------------------------------------------------
//    @GetMapping("/momo-payment")
//    public String doPost() throws Exception {
////
////        PartnerInfo devInfo = new PartnerInfo("MOMOLRJZ20181206", "mTCKt9W3eU1m39TW", "KqBEecvaJf1nULnhPF5htpG3AMtDIOlD");
////        //change the endpoint according to the appropriate processes
////
////        String commit = RequestType.CONFIRM_APP_TRANSACTION;
////        String rollback = RequestType.CANCEL_APP_TRANSACTION;
////        long amount = 10000;
////        String partnerRefId = String.valueOf(System.currentTimeMillis());
////        String partnerTransId = "1561046083186";
////        String requestId = String.valueOf(System.currentTimeMillis());
////        String momoTransId = "147938695";
////        String customerNumber = "0963181714";
////        String partnerName = "1561046083186";
////        String storeId = "1561046083186";
////        String storeName = "1561046083186";
////        String appData = "1561046083186";
////        String description = "Pay POS Barcode";
////        String paymentCode = "MM515023896957011876";
////
////        String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAlsL+G4UyFO0UQsQ4cAXuGYcn38d67PluKmeJqS2RcAqnNUFJjQieI5DSCyHVgAmPpUfDZ3CiSw+5NCfnjgChd/p4fq3bnGqSIw2JP78UROQDJYqfAc+WLvT29IgCH4O+P9+lOLUj2EWf8aqHxBwC1YPxtxK+8M+LKdVMAvZd3lXE3MBg9wTDYEcCNODXkNma/SfIKJCvmVWdKeKXd6IwW7yA0oTjdguAeqP8+O8jLjxJH57otRh63iX945vqAX2YAm9qzVoiDcWpv+UubRmbZ9l0moQwkdsDyCtCYPUtcW6kkdxuhlq8rg8RAVcinsz/843CBYHtqaUaAFQU1TO5EXiXT87zx/Oj2Bf4OC+iAJL/UQ4ASeL1vMoOfDSpSE8EnqKPyP+rM/H7oUaJrIin8KkrxmDLGQWKhNcTFO6UNPv3Hh13tEBv0GRy2vktL8+CWhrYHouXF2XwpS8uR/gH/Vl+5HT/HsTv/13gjSoGBQcdfyck9ZyHh5oBrQTds52C2vabCqWCEafRMbpj7lSrDWS2Df+XznR/hGkgewSdSZ/M0VK/DLadJ3x1Yhblv1HVw3jA3xzY1/zlNOZReLuvW6/kdRwJV/Zj5bd9eLJnz9jDPUcB0hAO+JuJYfTVuhZG9Beo1JbQ9+cFx+92ELn/yHDMod6rfrfBjikU9Gkxor0CAwEAAQ==";
////
////        // Uncomment to use the processes you need
////        // Make sure you are using the correct environment for each processes
////        // Change to valid IDs and information to use AppPay, POS, Refund processes.
////
////        //
////        AppPayResponse appProcessResponse = AppPay.process(Environment.selectEnv("dev", Environment.ProcessType.APP_IN_APP), partnerRefId, partnerTransId, amount, partnerName,
////                    storeId, storeName, publicKey, customerNumber, appData, description, Parameter.VERSION, Parameter.APP_PAY_TYPE);
////
////        // Extract the payment URL from the response
////        String paymentUrl = appProcessResponse.getTransid();
////
////        return paymentUrl;
////        POSPayResponse posProcessResponse = POSPay.process(Environment.selectEnv(Environment.EnvTarget.DEV, Environment.ProcessType.PAY_POS), partnerRefId, amount, "", "", publicKey, "", "MM943358184685515708", Parameter.VERSION, Parameter.APP_PAY_TYPE);
////
////        PayConfirmationResponse payConfirmationResponse = PayConfirmation.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_CONFIRM), "35646", rollback, requestId, "2305062978", "", "");
////
////        TransactionQueryResponse transactionQueryResponse = TransactionQuery.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_QUERY_STATUS), "1562298553079", "1562299067267", publicKey, "", Parameter.VERSION);
////        TransactionQuery.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_QUERY_STATUS), "1562299067267", "1562299177871", publicKey, "", Parameter.VERSION);
////
////        TransactionRefundResponse transactionRefundResponse = TransactionRefund.process(Environment.selectEnv("dev", Environment.ProcessType.PAY_QUERY_STATUS), "1562298553079", "", publicKey, "2305062760", amount, "", "1562299067267", Parameter.VERSION);
//
////        return appProcessResponse;
//
//
////        ------------------------------------------------------------
//        String requestId = String.valueOf(System.currentTimeMillis());
//        String orderId = String.valueOf(System.currentTimeMillis());
//        long amount = 50000;
//
//        String orderInfo = "Pay With MoMo";
//        String returnURL = "https://google.com.vn";
//        String notifyURL = "https://google.com.vn";
//        String extraData = "";
//        String bankCode = "SML";
//        String customerNumber = "0963181714";
//
//        Environment environment = Environment.selectEnv(Environment.EnvTarget.DEV, Environment.ProcessType.PAY_GATE);
//
////          Please uncomment the code to actually use the necessary All-In-One gateway payment processes
////          Remember to change the IDs
//
//        CaptureMoMoResponse captureMoMoResponse = CaptureMoMo.process(environment, orderId, requestId, Long.toString(amount), "", returnURL, notifyURL, "");
//        QueryStatusTransactionResponse queryStatusTransactionResponse = QueryStatusTransaction.process(environment, "1561972787557", "1562135830002");
//
////          Refund -- Manual Testing
////            RefundMoMoResponse response = RefundMoMo.process(environment, "1562135830002", orderId, "10000", "2304963912");
////            RefundStatus.process(environment, "1562135830002", "1561972787557");
////
//        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkpa+qMXS6O11x7jBGo9W3yxeHEsAdyDE" +
//                "40UoXhoQf9K6attSIclTZMEGfq6gmJm2BogVJtPkjvri5/j9mBntA8qKMzzanSQaBEbr8FyByHnf226dsL" +
//                "t1RbJSMLjCd3UC1n0Yq8KKvfHhvmvVbGcWfpgfo7iQTVmL0r1eQxzgnSq31EL1yYNMuaZjpHmQuT2" +
//                "4Hmxl9W9enRtJyVTUhwKhtjOSOsR03sMnsckpFT9pn1/V9BE2Kf3rFGqc6JukXkqK6ZW9mtmGLSq3" +
//                "K+JRRq2w8PVmcbcvTr/adW4EL2yc1qk9Ec4HtiDhtSYd6/ov8xLVkKAQjLVt7Ex3/agRPfPrNwIDAQAB";
//        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCSlr6oxdLo7XXHuMEaj1bfLF4cSwB3IMTjRSheGhB/0rpq21IhyVNkwQZ+rqCYmbYGiBUm0+SO+uLn+P2YGe0DyoozPNqdJBoERuvwXIHIed/bbp2wu3VFslIwuMJ3dQLWfRirwoq98eG+a9VsZxZ+mB+juJBNWYvSvV5DHOCdKrfUQvXJg0y5pmOkeZC5PbgebGX1b16dG0nJVNSHAqG2M5I6xHTewyexySkVP2mfX9X0ETYp/esUapzom6ReSorplb2a2YYtKrcr4lFGrbDw9WZxty9Ov9p1bgQvbJzWqT0Rzge2IOG1Jh3r+i/zEtWQoBCMtW3sTHf9qBE98+s3AgMBAAECggEAQxBiU9aFgnk5HFGDTwJrDRlASRNrOBUu3odCS6MDD2e6T67daYWw+HRy4zxDTu1r4JsbijMA6wUPEG/SnWanD8f26DAcGC5vFKvZv5Ki8bQIXVzDGhr5MRS/E3lDxuEqljSPN+1+Ch6CV9r/vmN/YBV6zC1hH3IrTRPD71Jj1KMITCDQlKcDbZqgFTY0wq2ONrzQ5lF0u1sSrdnHLny2kayIAocWqSVbfcSE/9iKN4jkc2/zBQOAFgBQVPuZOdLL+rf1PTKus75aJm/TzaCcoxF496kTw/mRJ77rOxB8mNDEhGULTopG0Bk12upA+QXzxsWJKm8pgv/iXV+0Hi27oQKBgQDCMAydxOCybtOnTkRQ66typlRJQDVgBCD4yhNchOd6jWk34GRY64MuNbyyrD8A5P/ioI4OvRs00S28Sb/G/w3ldciR0j7lm9FgbjkTDCrVVbp4P8gczgL+z5mPdCua1KQD+2C5RA2tMRJlAfczIVekoxCriuCQSO9RltsGT7LmEQKBgQDBP/bzTD+PKWmxeBOTLeNGH8IM63DeccWtowxRgeF1xohFK1ipi5RKxoKOVLxku0U3tKOe6thE2IhpaqYFcCRs2TFZidChyytEjD4LVlECfe9OvCqfVL8IvDUzw8B3850HYrGUh8y4Mmry3JJYLOKoAPBqEg9NLe9c8yI9rI3UxwKBgGVQjnSOMLHH8vPaePhDTUtfDqC9OFvlK5LCU8G0sdUWDKyTjad7ERE+BjqudZyw3fTO0e9MqPIwpQ0U6VMY5ZYvkrrKF/jSCDaoq2yNr5doyAZPOMgWkCeEBtl6wflhMkXFlNx0bjJLZQ6ALQpnPgPu9BacObft5bcK3zF2yZ8RAoGBAIgkYfuBKf3XdQh7yX6Ug1qxoOmtLHTpvhPXnCQH1ig811+za+D13mDXfL5838QvUlIuRl78n6PQ0DlD0vZdzKuKT4P+3SY+lZrTGhqukp+ozOCxG23oLDUhMnHnZD6dN3EujGBRU14o1sOFtOu9o2gsUTLIylLbG5hmCSdd2wWdAoGBAIvddYHkS1b8B8TCv1+CVObe5WCUvqpZgbHo3oztD0KxlgWvl+f6y8DUToK3KU9sp512Ivk43mn1Xv2QftBx8E4vyhWeltdiKOJOhMsk6djjoyb8AOuyPVumXTQBuue1yRrTKLAl1SaZnzdrKzpXsI8OBpnI0bjFxA2SNnU/iD0R";
//
//        String partnerCode = "MOMOIQA420180417";
//        String phoneNumber = "0963181714";
//        String username = "nhat.nguyen";
//
//        orderId = String.valueOf(System.currentTimeMillis());
//        PayATMResponse payATMResponse = PayATM.process(environment, requestId, orderId, bankCode, "35000", "Pay With MoMo", returnURL, notifyURL, "");
//
////        orderId = String.valueOf(System.currentTimeMillis());
////        RefundATM.process(environment, orderId, "1561972550332", "10000", "2304962904", bankCode);
////        RefundStatus.process(environment, "1562135830002", "1561972787557");
//        generateRSA(customerNumber, "247", "247", "nhatnguyen", environment.getPartnerInfo().getPartnerCode(), amount, publicKey);
//        return "https://payment.momo.vn/pay/app"+ payATMResponse.getBankCode();
//
////        return
//    }
//
//    //generate RSA signature from given information
//    public static String generateRSA(String phoneNumber, String billId, String transId, String username, String partnerCode, long amount, String publicKey) throws Exception {
//        // current version of Parameter key name is 2.0
//        Map<String, Object> rawData = new HashMap<>();
//        rawData.put(Parameter.CUSTOMER_NUMBER, phoneNumber);
//        rawData.put(Parameter.PARTNER_REF_ID, billId);
//        rawData.put(Parameter.PARTNER_TRANS_ID, transId);
//        rawData.put(Parameter.USERNAME, username);
//        rawData.put(Parameter.PARTNER_CODE, partnerCode);
//        rawData.put(Parameter.AMOUNT, amount);
//
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(rawData);
//        byte[] testByte = jsonStr.getBytes(StandardCharsets.UTF_8);
//        String hashRSA = Encoder.encryptRSA(testByte, publicKey);
//
//        return hashRSA;
//    }

    @PostMapping("/momo-payment")
    public String doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        PartnerInfo devInfo = new PartnerInfo("MOMOLRJZ20181206", "mTCKt9W3eU1m39TW", "SetA5RDnLHvt51AULf51DyauxUo3kDU6");
//        Environment env = new Environment("https://test-payment.momo.vn/v2/gateway/api", devInfo, "development");

        PartnerInfo devInfo = new PartnerInfo("MOMOBKUN20180529", "klm05TvNBzhg7h7j", "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa");
        Environment env = new Environment("https://test-payment.momo.vn/v2/gateway/api/create", devInfo, "development");

        String amount = "50000";

        String orderInfo = "Pay With MoMo";
        String returnURL = "https://google.com.vn";
        String notifyURL = "https://google.com.vn";
        String extraData = "";
        String bankCode = "SML";
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        String partnerClientId = "partnerClientId";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkpa+qMXS6O11x7jBGo9W3yxeHEsAdyDE" +
                "40UoXhoQf9K6attSIclTZMEGfq6gmJm2BogVJtPkjvri5/j9mBntA8qKMzzanSQaBEbr8FyByHnf226dsL" +
                "t1RbJSMLjCd3UC1n0Yq8KKvfHhvmvVbGcWfpgfo7iQTVmL0r1eQxzgnSq31EL1yYNMuaZjpHmQuT2" +
                "4Hmxl9W9enRtJyVTUhwKhtjOSOsR03sMnsckpFT9pn1/V9BE2Kf3rFGqc6JukXkqK6ZW9mtmGLSq3" +
                "K+JRRq2w8PVmcbcvTr/adW4EL2yc1qk9Ec4HtiDhtSYd6/ov8xLVkKAQjLVt7Ex3/agRPfPrNwIDAQAB";
        String customerNumber = "0963181714";
        System.out.println("PartnerInfo: " + devInfo);
        PayATM atmProcess = new PayATM(env);

        System.out.println("Environment: " + env);
        System.out.println("Partner Info: " + env.getPartnerInfo());

        PayATMRequest payATMRequest = atmProcess.createPayWithATMRequest(requestId, orderId, bankCode, amount, orderInfo, returnURL, notifyURL, extraData, env.getPartnerInfo());
//        payATMRequest.setAccessKey(publicKey);
//        payATMRequest.setSignature(generateRSA(customerNumber, "247", "247", "nhatnguyen", env.getPartnerInfo().getPartnerCode(), Long.parseLong(amount), publicKey));
//        PaymentResponse response1 = atmProcess.process(env, orderId, requestId, amount, orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);


// Debug: Print out the request object to verify values
        System.out.println("PayATMRequest: " + payATMRequest);

//        PayATMResponse payATMResponse = atmProcess.execute(payATMRequest);

//        PayATMResponse payATMResponse_1 = PayATM.process(env,requestId, orderId, bankCode, amount, orderInfo, returnURL, notifyURL, extraData);
// Debug: Print out the response object to verify values
        PayATMResponse payATMResponse1 = PayATM.process(env, requestId, orderId, bankCode, amount, orderInfo, returnURL, notifyURL, extraData);

        System.out.println("PayATMResponse: " + payATMResponse1);
//        System.out.println("PayATMResponse: " + payATMResponse1);
//        System.out.println(payATMResponse);
        return "dfsasadf" ;
    }


}
