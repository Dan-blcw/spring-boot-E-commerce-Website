package com.dan_michael.example.demo.model.dto.ob.sub;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubColor {
    public String color;
    public List<SubSizeQuantity> sizes;
}
