package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SubQuantityTotalResponse {
    private List<SubColor> quantityDetails;
    private String message;
}
