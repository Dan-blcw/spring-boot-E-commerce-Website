package com.dan_michael.example.demo.model.entities;

import com.dan_michael.example.demo.model.entities.SubEn.OrderDetail;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer identification_user;
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    private String address;
//    private String companyName;
    private String phoneNumber;
    private String emailAddress;

    private String shippingStatus;

    private String PaymentMethods;
    private Integer paymentStatus;

//    private Float unitPrice;
    private Float totalPrice;
    private Float shippingFee;
    private Float taxFee;

    private Integer percentDiscount;
    private Float TotalAmountOrder;

    private Integer orderStatus;

    @CreationTimestamp
    private Date createdAt;
}
