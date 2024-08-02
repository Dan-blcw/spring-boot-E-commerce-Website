package com.dan_michael.example.demo.config;

import com.dan_michael.example.demo.util.Constants;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.text.SimpleDateFormat;
import java.util.*;
@Configuration
public class VNPAYConfig {
    @Getter
    @Value("${payment.vnPay.url}")
    private String vnp_PayUrl;
    @Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;
    @Value("${payment.vnPay.tmnCode}")
    private String vnp_TmnCode ;
    @Getter
    @Value("${payment.vnPay.secretKey}")
    private String secretKey;
    @Value("${payment.vnPay.version}")
    private String vnp_Version;
    @Value("${payment.vnPay.command}")
    private String vnp_Command;
    @Value("${payment.vnPay.orderType}")
    private String orderType;

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put(Constants.Vnp_Version, this.vnp_Version);
        vnpParamsMap.put(Constants.Vnp_Command, this.vnp_Command);
        vnpParamsMap.put(Constants.Vnp_TmnCode, this.vnp_TmnCode);
        vnpParamsMap.put(Constants.Vnp_CurrCode, Constants.VND_Uppercase);
        vnpParamsMap.put(Constants.Vnp_TxnRef,  VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put(Constants.Vnp_OrderInfo, Constants.Payment_Orders +  VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put(Constants.Vnp_OrderType, this.orderType);
        vnpParamsMap.put(Constants.Vnp_Locale, Constants.Vnp_Locale_RN);
        vnpParamsMap.put(Constants.Vnp_ReturnUrl, this.vnp_ReturnUrl);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Constants.Time_Zone));
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.Time_Format);
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put(Constants.Vnp_CreateDate, vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put(Constants.Vnp_ExpireDate, vnp_ExpireDate);
        return vnpParamsMap;
    }
}
