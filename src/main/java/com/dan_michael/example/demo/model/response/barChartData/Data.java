package com.dan_michael.example.demo.model.response.barChartData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("labels")
    private List<String> labels;
    @JsonProperty("data")
    private List<Float> data;
}
