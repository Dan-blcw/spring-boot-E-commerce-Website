package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.model.entities.SubEn.Brand;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class CategoryDtos {
    private List<String> brands;
    public String categoryName;
    public String sku;
    public Integer status;
//    private MultipartFile image;
}
