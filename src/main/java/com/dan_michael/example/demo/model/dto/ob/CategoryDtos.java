package com.dan_michael.example.demo.model.dto.ob;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
