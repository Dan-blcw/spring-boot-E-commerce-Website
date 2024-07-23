package com.dan_michael.example.demo.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ProductImg> images;
    private String name;
    @Column(length = 10485760)
    private String description;
    private Float price;
    private Integer quantity;
    private String category;
    private String colour;
    private String size;
    private Float rating;
    private Integer nRating;
    private Boolean favourite;
    private Boolean saleStatus;
    private Float salePrice;
    private Boolean newStatus;

    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Comment> comments;

    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private Date createDate;

    @CreatedBy
    @Column(
            nullable = false,
            updatable = false
    )
    private Integer createdByUserid;

}
