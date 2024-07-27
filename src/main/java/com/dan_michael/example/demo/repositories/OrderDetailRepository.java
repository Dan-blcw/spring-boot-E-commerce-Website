package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.model.entities.OrderDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrderDetailRepository  extends JpaRepository<OrderDetail, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM OrderDetail od WHERE od.identification_order = :identificationOrder")
    void deleteByIdentificationOrder(@Param("identificationOrder") Integer identificationOrder);
    @Query("SELECT pi FROM OrderDetail pi WHERE pi.identification_order = :identificationOrder")
    List<OrderDetail> findByIdentification_order(@Param("identificationOrder") Integer identificationOrder);
}