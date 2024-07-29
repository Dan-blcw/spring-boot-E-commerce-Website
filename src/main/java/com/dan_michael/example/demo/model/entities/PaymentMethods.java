package com.dan_michael.example.demo.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentMethods {

    @Id
    @GeneratedValue
    public Integer id;
    @Column(unique = true)
    public String name;
    @Column(length = 10485760)
    public String description;
    public Date createdDate;
    public Integer status;
//    private String name;
//    private byte[] image;
//    private String image_url;
}
