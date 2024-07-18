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
@Table(name = "productImg")
public class ProductImg {

    @Id
    @GeneratedValue
    private Integer id;

    @Lob
    @Column(name = "image", columnDefinition = "bigint")
    private byte[] image;
}