package com.dan_michael.example.demo.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class SubCategoryResponse {
    private Integer id;
    private String sku;
    private String name;
    private String image_url;
    private List<String> brands;
    private Date date;
    private Integer status;
}
