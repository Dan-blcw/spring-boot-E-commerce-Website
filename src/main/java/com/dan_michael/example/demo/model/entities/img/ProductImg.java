package com.dan_michael.example.demo.model.entities.img;

import com.dan_michael.example.demo.model.entities.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class ProductImg {

    @Id
    @GeneratedValue
    private Integer id;

    @Lob
    @Column(name = "image", columnDefinition = "bigint")
    private byte[] image;

    private String identification;

    private String imageName;
    private String img_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
}