package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
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
public class StatisticsResponse {

    private Integer nTotalCustomer;

    private Integer nTotalProductActive;
    private Integer nTotalQuantitySold;
    //ndoanh thu
    private Float nTotalRevenue;
    private Float nTotalRevenueThisMonth;

    //nOrder
    private Integer nTotalOrders;
    private Integer nTotalOrdersThisMonth;

    private Integer nTotalWaitingQuestion;
    private Integer nTotalRating;

    @JsonProperty("proBestSelling")
    private List<ProductResponse> proBestSelling;
//    private List<BestSellingResponse> proBestSelling;
}
