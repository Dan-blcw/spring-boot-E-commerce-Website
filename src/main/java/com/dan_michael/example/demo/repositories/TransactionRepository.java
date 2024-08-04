package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
