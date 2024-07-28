package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.entities.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImgResponse {

    private Integer id;
    private String identification;
    private String imageName;
    private String img_url;

}
