package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Integer> {
//    Cart findByUser(User user);

    @Query("SELECT pi FROM Cart pi WHERE pi.identification_user = :identification_user")
    Cart findByIdentification(@Param("identification_user") Integer identification_user);
}
