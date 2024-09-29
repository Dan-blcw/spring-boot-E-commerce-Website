package com.dan_michael.example.demo.model.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNo;
    private String accountName;
    private String acqId;
    private String addInfo;
    private String amount;
    private String template;


    private String skuOrder;
    private String paymentMethods;
    private Integer paymentStatus;
    public Date transactionDate;
}
