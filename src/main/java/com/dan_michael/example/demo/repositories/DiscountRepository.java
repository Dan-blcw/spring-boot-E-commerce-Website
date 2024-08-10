package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Discount;
import com.dan_michael.example.demo.model.entities.TradeMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    @Query("SELECT pi FROM Discount pi WHERE pi.sku = :sku")
    Discount findBySku(@Param("sku") String sku);
}