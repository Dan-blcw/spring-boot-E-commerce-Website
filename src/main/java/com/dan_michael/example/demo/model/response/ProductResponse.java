package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.entities.Comment;
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
    private String imageMain;
    private String skuQa;
//    private List<SubImgResponse> images;
    private List<String> images;
    private List<String> colours;
    private List<String> sizes;
    private String subCategory;
    private String name;
    private String description;

    private List<SubColor> quantityDetails;
    private Integer totalQuantity;
    private String category;
    private String tradeMask;
    private Float rating;
    private Integer nRating;

    private Integer quantitySold;
    private String style;
    private String material;

    private List<Integer> favourite;

    private Float originalPrice ;
    private Float saleDiscountPercent ;
    private Float finalPrice ;
    private Boolean saleStatus;
    private Boolean isFavorite;


    private Boolean newStatus;
    private List<Comment> comments;
    private Date createDate;
    private Integer createdByUserid;
}
