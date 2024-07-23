package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.entities.ProductImg;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class CategoryDtos {
    public String categoryName;
    public String description;
    public Date createdDate;
    public Integer status;
    private byte[] image;
}
