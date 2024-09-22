package com.dan_michael.example.demo.model.response;

import com.dan_michael.example.demo.model.entities.Role;
import com.dan_michael.example.demo.model.entities.img.UserImg;
import jakarta.persistence.*;
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
public class DetailProfile {
    private String name;
    private String username;
    private String email;
    private String password;
    private String companyName;
    private String address;
    private String phoneNumber;
    private Date date_joined;
    private Date last_login;
    private Date last_update;
    private Integer is_active;
    private Boolean useFirstDiscount;
    private String userImgUrl;
}