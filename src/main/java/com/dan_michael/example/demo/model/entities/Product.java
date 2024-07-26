package com.dan_michael.example.demo.model.entities;

import com.dan_michael.example.demo.model.entities.SubEn.Brands;
import com.dan_michael.example.demo.model.entities.SubEn.Colors;
import com.dan_michael.example.demo.model.entities.SubEn.FavouriteProduct;
import com.dan_michael.example.demo.model.entities.SubEn.Sizes;
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
import java.util.List;

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
    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Colors> colours;
    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Sizes> sizes;
    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Brands> brands;

    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<FavouriteProduct> favourite;

    private String name;
    @Column(length = 10485760)
    private String description;

    private Integer quantity;
    private String category;

    private Float rating;
    private Integer nRating;


    private Float originalPrice ;
    private Float saleDiscountPercent ;
    private Float finalPrice ;
    private Boolean saleStatus;

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
