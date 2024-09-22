package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.entities.SubEn.OrderDetail;
import com.dan_michael.example.demo.model.entities.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Integer order_id;
    private Integer user_id;
    private List<SubCart_OrderResponse> orderDetails;
    private String address;

    private String phoneNumber;
    private String skuOrder;
    private String emailAddress;

    private String paymentMethods;
    private Integer paymentStatus;

//    private String shippingStatus;

    private Float unitPrice;
    private Float shippingFee;
    private Float taxFee;

    private Integer percentDiscount;
    private Float totalPayment;
    private Integer totalQuantity;

    private String orderStatus;
}
