package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.SubEn.CartDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {
    @Query("SELECT pi FROM CartDetail pi WHERE pi.identification_cart = :identificationCart")
    List<CartDetail> findByIdentification_cart(@Param("identificationCart") Integer identificationCart);


    @Query("SELECT pi FROM CartDetail pi WHERE pi.identification_cart = :identificationCart")
    CartDetail findByIdentification_cart_(@Param("identificationCart") Integer identificationCart);

    @Query("SELECT pi FROM CartDetail pi WHERE pi.identification_cart = :identificationCart and pi.color = :color and  pi.size =:size")
    CartDetail findByIdentification_cart_(
            @Param("identificationCart") Integer identificationCart,
            @Param("color") String color,
                    @Param("size") String size
    );
    @Transactional
    @Modifying
    @Query("DELETE FROM CartDetail od WHERE od.identification_cart = :identification_cart")
    void deleteByIdentificationCart(@Param("identification_cart") Integer identification_cart);
}
