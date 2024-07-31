package com.dan_michael.example.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubImgResponse {

    private Integer id;
    private String identification;
    private String imageName;
    private String img_url;

}
