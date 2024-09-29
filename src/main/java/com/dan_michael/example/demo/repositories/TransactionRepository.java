package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.QRInfo;
import com.dan_michael.example.demo.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT pi FROM Transaction pi WHERE pi.skuOrder = :skuOrder")
    Transaction findTransactionBySkuOrder(@Param("skuOrder") String skuOrder);
}
