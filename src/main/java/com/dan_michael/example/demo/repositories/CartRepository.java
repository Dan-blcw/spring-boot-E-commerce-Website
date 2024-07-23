package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Cart;
import com.dan_michael.example.demo.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUser(User user);
}
