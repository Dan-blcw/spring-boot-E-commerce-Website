package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Cart;
import com.dan_michael.example.demo.model.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT pi FROM Order pi WHERE pi.identification_user = :identification_user")
    List<Order> findByAllOrderByUser(@Param("identification_user") Integer identification_user);
    @Query("SELECT pi FROM Order pi WHERE pi.orderStatus IS NOT NULL AND pi.orderStatus <> 'Đã Hủy'")
    List<Order> findByAllOrderActive();
    @Query("SELECT pi FROM Order pi WHERE  pi.orderStatus =  :orderStatus")
    List<Order> findByAllOrderByAdmin_OrderStatus(
            @Param("orderStatus") String orderStatus
    );
    @Query("SELECT pi FROM Order pi WHERE pi.identification_user = :identification_user AND pi.paymentStatus = :paymentStatus")
    List<Order> findByAllOrderByUser_PaymentStatus(
            @Param("identification_user") Integer identification_user,
            @Param("paymentStatus") Integer paymentStatus
    );
//in transit
//received
//cancelled
    @Query("SELECT pi FROM Order pi WHERE pi.identification_user = :identification_user AND pi.orderStatus =  :orderStatus")
    List<Order> findByAllOrderByUser_OrderStatus(
            @Param("identification_user") Integer identification_user,
            @Param("orderStatus") String orderStatus
    );

    @Query("SELECT pi FROM Order pi WHERE pi.identification_user = :identification_user AND pi.orderStatus =  :orderStatus AND pi.paymentStatus = :paymentStatus")
    List<Order> findByAllOrderByUser_Both(
            @Param("identification_user") Integer identification_user,
            @Param("paymentStatus") Integer paymentStatus,
            @Param("orderStatus") String orderStatus
    );
}