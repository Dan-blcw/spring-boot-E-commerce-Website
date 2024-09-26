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
public class ChartData {
    @JsonProperty("datasets")
    private List<Data> datasets;
}
