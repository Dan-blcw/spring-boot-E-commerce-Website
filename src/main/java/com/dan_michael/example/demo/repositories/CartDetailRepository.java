package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Cart;
import com.dan_michael.example.demo.model.entities.CartDetail;
import com.dan_michael.example.demo.model.entities.OrderDetail;
import com.dan_michael.example.demo.model.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {
    List<CartDetail> findByCartAndProductAndColorsAndSize(Cart cart, Product product, String colors, String size);

    List<CartDetail> findByCart(Cart cart);

    @Query("SELECT pi FROM CartDetail pi WHERE pi.identification_cart = :identificationCart")
    List<CartDetail> findByIdentification_cart(@Param("identificationCart") Integer identificationCart);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartDetail od WHERE od.identification_cart = :identification_cart")
    void deleteByIdentificationCart(@Param("identification_cart") Integer identification_cart);
}
