package com.dan_michael.example.demo.model.entities;

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
public class OrderDetail {
    @Id
    @GeneratedValue
    private Integer id;

//    private List<Products_Order>
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private Integer product_id;
    private String quantity;
}
