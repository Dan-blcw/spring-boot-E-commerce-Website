//package com.dan_michael.example.demo.config;
//
//import com.dan_michael.example.demo.model.dto.ob.ItemDetailDto;
//import com.paypal.api.payments.*;
//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.PayPalRESTException;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//@Service
//public class PaypalConfig {
//    @Getter
//    @Value("${payment.paypal.clientId}")
//    private String CLIENT_ID = "AWDqATAEKGD1PwKN7uTPIBlG94ky6YjfiD6ceJRJT5cciM4kvV3Uff4mYqp4ZrUaRR0RWYBnIxorHfdZ";
//    @Getter
//    @Value("${payment.paypal.clientSecret}")
//    private String CLIENT_SECRET = "EJ7H7aISC64EoFOOI1eqGg8wPSRLs7C2w5upPCsN-3Ftd27zJFbCPo_-K__VgHp5e0GXWCmiNG75P-P7";
//    @Getter
//    @Value("${payment.paypal.mode}")
//    private String MODE = "sandbox";
//
//    public String authorizePayment(ItemDetailDto itemDetailDto) throws PayPalRESTException {
//        Payer payer = getPayerInfo();
//        RedirectUrls redirectUrls = getRedirectURLs();
//        List<Transaction> listTransactions = getTransactionInfo(itemDetailDto);
//
//        Payment requestPayment = new Payment();
//        requestPayment.setTransactions(listTransactions)
//                .setRedirectUrls(redirectUrls)
//                .setPayer(payer)
//                .setIntent("authorize");
//
//        APIContext apiContext = new APIContext(CLIENT_ID,CLIENT_SECRET,MODE);
//        Payment aprrovePayment = requestPayment.create(apiContext);
//        System.out.println(aprrovePayment.toString());
//        return getApprovalLink(aprrovePayment);
//    }
//
//
//    private String getApprovalLink(Payment approvedPayment){
//        List<Links> links = approvedPayment.getLinks();
//        String aprrovalLink = null;
//        for (Links link : links){
//            if(link.getRel().equalsIgnoreCase("approval_url")){
//                aprrovalLink = link.getHref();
//            }
//        }
//        return aprrovalLink;
//    }
//
//    private List<Transaction> getTransactionInfo(ItemDetailDto itemDetailDto){
//        Details details = new Details();
//        details.setShipping(String.format("%.3f", itemDetailDto.getShippingFee()));
//        details.setSubtotal(String.format("%.3f", itemDetailDto.getSubTotal()));
//        details.setTax(String.format("%.3f", itemDetailDto.getTaxFee()));
//
//        Amount amount = new Amount();
//        amount.setCurrency("USD");
//        amount.setTotal(String.valueOf(itemDetailDto.getTotal()));
//
//        Transaction transaction = new Transaction();
//        transaction.setAmount(amount);
//        transaction.setDescription("Description Paypal ProductName");
//
//        ItemList itemList = new ItemList();
//        List<Item> items = new ArrayList<Item>();
//
//        Item item = new Item();
//        item.setCurrency("USD")
//                .setName("Description Paypal")
//                .setPrice(String.format("%.3f", itemDetailDto.getSubTotal()))
//                .setTax(String.format("%.3f", itemDetailDto.getTaxFee()))
//                .setQuantity(String.valueOf(itemDetailDto.getQuantity()));
//        items.add(item);
//        itemList.setItems(items);
//        transaction.setItemList(itemList);
//
//        List<Transaction> list = new ArrayList<Transaction>();
//        list.add(transaction);
//
//        return list;
//    }
//    private RedirectUrls getRedirectURLs(){
//        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setCancelUrl("http://localhost:8080/api/v1/payment/paypaltest");
//        redirectUrls.setReturnUrl("http://localhost:8080/api/v1/payment/review_payment");
//        return  redirectUrls;
//    }
//    public Payer getPayerInfo(){
//        Payer payer = new Payer();
//        payer.setPaymentMethod("paypal");
//
//        PayerInfo payerInfo = new PayerInfo();
//        payerInfo.setFirstName("ecommerce")
//                .setLastName("demo47")
//                .setEmail("ecommercedemo47@gmail.com");
//        payer.setPayerInfo(payerInfo);
//        return payer;
//    }
//}
package com.dan_michael.example.demo.service.Payment;

        import com.dan_michael.example.demo.model.dto.ob.ItemDetailDto;
        import com.dan_michael.example.demo.model.dto.ob.OrderDtos;
        import com.dan_michael.example.demo.model.response.OrderResponse;
        import com.dan_michael.example.demo.repositories.ProductRepository;
        import com.dan_michael.example.demo.util.Constants;
        import com.paypal.api.payments.*;
        import com.paypal.base.rest.APIContext;
        import com.paypal.base.rest.PayPalRESTException;
        import lombok.Getter;
        import lombok.RequiredArgsConstructor;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Service;

        import java.text.DecimalFormat;
        import java.util.ArrayList;
        import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentPaypalService {

    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(PaymentPaypalService.class);

    @Getter
    @Value("${payment.paypal.clientId}")
    private String CLIENT_ID = "AWDqATAEKGD1PwKN7uTPIBlG94ky6YjfiD6ceJRJT5cciM4kvV3Uff4mYqp4ZrUaRR0RWYBnIxorHfdZ";
    @Getter
    @Value("${payment.paypal.clientSecret}")
    private String CLIENT_SECRET = "EJ7H7aISC64EoFOOI1eqGg8wPSRLs7C2w5upPCsN-3Ftd27zJFbCPo_-K__VgHp5e0GXWCmiNG75P-P7";
    @Getter
    @Value("${payment.paypal.mode}")
    private String MODE = "sandbox";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");

    public String authorizePayment(OrderResponse orderDtos) throws PayPalRESTException {
        logger.info("Authorizing payment for item detail: {}", orderDtos);

        Payer payer = getPayerInfo();
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransactions = getTransactionInfo(orderDtos);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransactions)
                .setRedirectUrls(redirectUrls)
                .setPayer(payer)
                .setIntent("authorize");

        logger.info("Creating APIContext with clientId: {}, clientSecret: {}, mode: {}", CLIENT_ID, CLIENT_SECRET, MODE);
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        Payment approvePayment = requestPayment.create(apiContext);
        logger.info("Approved Payment: {}", approvePayment);

        return getApprovalLink(approvePayment);
    }

    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;
        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }
        return approvalLink;
    }

    private List<Transaction> getTransactionInfo(OrderResponse orderDtos) {

        Details details = new Details();
        details.setShipping(formatAmount(orderDtos.getShippingFee()));
        details.setSubtotal(formatAmount(orderDtos.getUnitPrice()));
        details.setTax(formatAmount(orderDtos.getTaxFee()));

        Amount amount = new Amount();
        amount.setCurrency("VND");
        amount.setTotal(formatAmount(orderDtos.getTotalPayment()));
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Description of payment !");

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        for (var x: orderDtos.getOrderDetails()) {
            Item item = new Item();
            item.setCurrency("VND")
                    .setName(x.getName())
                    .setPrice(formatAmount(x.getUnitPrice() )) // Ensure the price is per item
//                    .setTax(formatAmount(orderDtos.getTaxFee())) // Ensure the tax is per item
                    .setQuantity(String.valueOf(x.getQuantity()));

            items.add(item);
        }
        itemList.setItems(items);
        transaction.setItemList(itemList);
        List<Transaction> list = new ArrayList<>();
        list.add(transaction);

        return list;
    }

    private String formatAmount(Float amount) {
        return DECIMAL_FORMAT.format(amount);
//        return String.valueOf(amount);
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/api/v1/payment/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/api/v1/payments/paypal-review-payment");
        return redirectUrls;
    }

    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        APIContext apiContext = new APIContext(CLIENT_ID,CLIENT_SECRET,MODE);
        return Payment.get(apiContext,paymentId);
    }

    public Payer getPayerInfo() {
        Payer payer = new Payer();
        payer.setPaymentMethod(Constants.PayPal_Name);

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(Constants.First_Name_Of_Company)
                .setLastName(Constants.First_Name_Of_Company)
                .setEmail(Constants.Email_Of_Company);
        payer.setPayerInfo(payerInfo);
        return payer;
    }
}