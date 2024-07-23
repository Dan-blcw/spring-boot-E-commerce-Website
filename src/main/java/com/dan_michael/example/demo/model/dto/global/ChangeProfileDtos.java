package com.dan_michael.example.demo.model.dto.global;

import com.dan_michael.example.demo.model.entities.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ChangeProfileDtos {
//    private String id;
//    private Role role;
    private String name;
    private String username;
    private String email;
    private String address;
    private String phoneNumber;
//    private Date date_joined;
//    private Date last_login;
    private Integer is_active;
}
