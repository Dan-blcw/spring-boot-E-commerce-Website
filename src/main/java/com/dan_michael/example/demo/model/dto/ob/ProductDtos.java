package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductDtos {
    private List<MultipartFile> images;
    private String name;
    private String description;

    private List<SubColor> quantityDetails;
    private String category;

    private String brand;

    private Boolean favourite;

    private Float originalPrice ;
    private Float saleDiscountPercent ;
    private Boolean saleStatus;

    private Boolean newStatus;

    private Integer createdByUserid;
}
