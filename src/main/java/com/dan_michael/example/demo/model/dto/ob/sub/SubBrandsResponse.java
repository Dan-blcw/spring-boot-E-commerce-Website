package com.dan_michael.example.demo.model.dto.ob.sub;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SubBrandsResponse {
    private List<String> brands;
    private String message;
}
