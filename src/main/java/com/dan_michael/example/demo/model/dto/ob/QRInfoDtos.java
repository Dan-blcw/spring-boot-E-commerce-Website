package com.dan_michael.example.demo.model.dto.ob;

import jakarta.persistence.Column;
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
public class QRInfoDtos {

    private String accountNo;
    private String accountName;
    private String acqId;
    private String template;

    private Integer status;
    public String createBy;
}
