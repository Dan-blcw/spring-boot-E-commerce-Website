package com.dan_michael.example.demo.model.dto.ob;
import com.dan_michael.example.demo.model.dto.global.PaginationDto;
import com.dan_michael.example.demo.model.entities.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDtos {

    @JsonProperty("data")
    private List<Product> data;

    @JsonProperty("pagination")
    private PaginationDto paginationDto;
}

