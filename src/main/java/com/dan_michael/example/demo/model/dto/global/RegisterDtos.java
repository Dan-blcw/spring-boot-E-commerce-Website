package com.dan_michael.example.demo.model.dto.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDtos {
    private String name;
    private String email;
    private String password;
    private String otpCode;

//  Demo Ảnh
    private String img_url;
}
