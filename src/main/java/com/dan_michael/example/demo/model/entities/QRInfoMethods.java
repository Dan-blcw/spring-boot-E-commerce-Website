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
public class QRInfoMethods {

    @Id
    @GeneratedValue
    public Integer id;
    @Column(unique = true)
    public Integer accountNo;
    public String accountName;
    @Column(length = 10485760)
    public String description;

}
