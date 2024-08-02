package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.dto.ob.sub.SubColorListDeserializer;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDtos {
    private List<MultipartFile> images;
    private String name;

    private String description;

    @JsonDeserialize(using = SubColorListDeserializer.class)
    private List<SubColor> quantityDetails;
    private String category;
    private String brand;
    private String tradeMask;

    private Float originalPrice ;
    private Float saleDiscountPercent ;

    private String imageMain;
    private Integer createdByUserid;
}
