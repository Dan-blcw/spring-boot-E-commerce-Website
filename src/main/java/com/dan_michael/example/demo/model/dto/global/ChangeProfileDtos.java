package com.dan_michael.example.demo.model.dto.global;

import com.dan_michael.example.demo.model.entities.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@Builder
public class ChangeProfileDtos {
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
}
