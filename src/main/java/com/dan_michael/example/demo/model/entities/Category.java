package com.dan_michael.example.demo.model.entities;

import com.dan_michael.example.demo.model.entities.SubEn.Brand;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.dan_michael.example.demo.model.entities.SubEn.Brand;



import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue
    public Integer id;
    @Column(unique = true)
    public String name;

    private String sku;

    @OneToMany(mappedBy = "category",fetch = FetchType.EAGER)
    private List<Brand> brand;

    private Date createdDate;
    private Integer status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
}
