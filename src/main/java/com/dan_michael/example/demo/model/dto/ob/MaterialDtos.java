package com.dan_michael.example.demo.model.dto.ob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDtos {
    private String materialName;
    private Integer id;
    private String description;
    public String image_url;
    public Integer status;
}