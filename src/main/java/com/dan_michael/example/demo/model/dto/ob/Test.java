package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.entities.SubEn.QuantityDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Test {
    @JsonProperty("name")
    private String name;
//    @JsonProperty("colours")
//    private List<String> colours;
//    @JsonProperty("sizes")
//    private List<String> sizes;

    @JsonProperty("sizes")
    private List<QuantityDetail> quantityDetails;
    @JsonProperty("brands")
    private String brands;
}
