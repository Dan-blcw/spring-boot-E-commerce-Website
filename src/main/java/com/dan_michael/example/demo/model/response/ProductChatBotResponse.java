package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.entities.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductChatBotResponse {
    private String imageMain;
    private String sku;
    private String name;
    private Float originalPrice ;
    private Float finalPrice ;
}
