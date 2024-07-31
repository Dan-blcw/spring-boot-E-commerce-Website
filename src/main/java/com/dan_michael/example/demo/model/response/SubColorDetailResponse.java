package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SubColorDetailResponse {
    private String color;
    private List<SubSizeQuantity> sizes;
    private String message;
}
