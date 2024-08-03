package com.dan_michael.example.demo.model.entities.SubEn;

import com.dan_michael.example.demo.model.entities.Cart;
import com.dan_michael.example.demo.model.entities.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    private Integer identification_cart;

    private Integer product_identification;
    private String name;
    private String image;
    private int quantity;
    private String color;
    private String size;
    private Float unitPrice;
    private Float totalPrice;
}