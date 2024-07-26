package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.model.entities.SubEn.Brands;
import com.dan_michael.example.demo.model.entities.SubEn.Colors;
import com.dan_michael.example.demo.model.entities.SubEn.Sizes;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Integer id;
    private List<ProductImg> images;
    private List<String> colours;
    private List<String> sizes;
    private List<String> brands;
    private String name;
    private String description;

    private Integer quantity;
    private String category;
    private Float rating;
    private Integer nRating;

    private List<Integer> favourite;

    private Float originalPrice ;
    private Float saleDiscountPercent ;
    private Float finalPrice ;
    private Boolean saleStatus;

    private Boolean newStatus;
    private List<Comment> comments;
    private Date createDate;
    private Integer createdByUserid;
}
