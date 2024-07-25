package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, Integer> {
    Optional<PaymentMethods> findPaymentMethodsByName(String name);

    @Query("SELECT pi FROM PaymentMethods pi WHERE pi.name = :name")
    PaymentMethods findPaymentMethodsByName_(@Param("name")String name);

}
