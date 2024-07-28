package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.dto.ob.sub.SubQuantity;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class ProductDtos {
    private List<MultipartFile> images;
    private String name;
    private String description;

    private List<SubQuantity> quantityDetail;
    private String category;

    private String brand;

    private Boolean favourite;

    private Float originalPrice ;
    private Float saleDiscountPercent ;
    private Boolean saleStatus;

    private Boolean newStatus;

    private Integer createdByUserid;
}
