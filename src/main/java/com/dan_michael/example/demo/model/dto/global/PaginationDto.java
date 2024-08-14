package com.dan_michael.example.demo.model.dto.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDto {
    private Integer _limit;
    private Integer _page;
}
