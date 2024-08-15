package com.dan_michael.example.demo.model.dto.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDtos {
    private String product_name;
    private Integer use_id;
}
