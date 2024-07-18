package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository  extends JpaRepository<OrderDetail, Integer> {
}