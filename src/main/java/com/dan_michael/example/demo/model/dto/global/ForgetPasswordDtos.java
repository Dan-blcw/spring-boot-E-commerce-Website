package com.dan_michael.example.demo.model.dto.global;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class ForgetPasswordDtos {
    private String email;
    private String name;
}
