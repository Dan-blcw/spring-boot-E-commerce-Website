package com.dan_michael.example.demo.model.entities.SubEn;

import com.dan_michael.example.demo.model.entities.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DetailSizeQuantity {
    @Id
    @GeneratedValue
    private Integer id;
    private String size;
    private Integer quantity;
    private String identification;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quantity_detail_id")
    @JsonBackReference
    private QuantityDetail quantityDetail;
}