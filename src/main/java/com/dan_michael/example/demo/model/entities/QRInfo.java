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
public class QRInfo {

    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    private String accountNo;
    private String accountName;
    private String acqId;
    private String template;

    private Integer status;
    public Date createAt;
    public String createBy;
}
