package com.dan_michael.example.demo.model.entities;

import com.dan_michael.example.demo.model.entities.SubEn.*;
import com.dan_michael.example.demo.model.entities.img.ProductImg;
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
    private String skuQa;
    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ProductImg> images;

    private String imageMain;
    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<QuantityDetail> quantityDetails;

    private Integer totalQuantity;

    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<FavouriteProduct> favourite;

    private String name;
    @Column(length = 10485760)
    private String description;

    private Integer quantitySold;
    private String style;
    private String material;

    private String category;
    private String tradeMask;
    private String subCategory;

    private Float rating;
    private Integer nRating;


    private Float originalPrice ;
    private Float saleDiscountPercent ;
    private Float finalPrice ;
    private Boolean saleStatus;
//    private Boolean isFavorite;

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

    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private Date updateDate;

    @CreatedBy
    @Column(
            nullable = false,
            updatable = false
    )
    private Integer createdByUserid;

}
