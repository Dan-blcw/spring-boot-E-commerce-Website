package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.ProductImg;
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
    private Float price;
    private Integer quantity;
    private String category;
    private String colour;
    private String size;
    private Boolean saleStatus;
    private Float salePrice;
    private Integer createdByUserid;
}
