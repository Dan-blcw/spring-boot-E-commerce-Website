package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, Integer> {
    @Query("SELECT pi FROM PaymentMethods pi WHERE pi.name = :name")
    PaymentMethods findPaymentMethodsByName_(@Param("name")String name);

    @Query("SELECT pi FROM PaymentMethods pi WHERE pi.status = 1")
    List<PaymentMethods> findAllMethodsActive();
}
