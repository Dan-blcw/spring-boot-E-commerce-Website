package com.dan_michael.example.demo.model.dto.global;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordDtos {
    private String old_password;
    private String new_password;
    private String renew_password;
}
