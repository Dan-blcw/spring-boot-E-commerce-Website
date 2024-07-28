package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.model.entities.SubEn.QuantityDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Integer id;
    private List<ProductImgResponse> images;
    private List<String> colours;
    private List<String> sizes;
    private String brand;
    private String name;
    private String description;

    private List<QuantityDetail> quantity;
    private Integer totalQuantity;
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
