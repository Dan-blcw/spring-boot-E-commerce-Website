package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.SubEn.FavouriteProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouriteProductRepository extends JpaRepository<FavouriteProduct, Integer> {
    @Query("SELECT pi FROM FavouriteProduct pi WHERE pi.identification = :identification")
    List<FavouriteProduct> findFavouriteByIdentification(@Param("identification") String identification);
    @Transactional
    @Modifying
    @Query("DELETE FROM FavouriteProduct od WHERE od.user_id = :user_id AND  od.identification = :identification")
    void deleteByUser_id(@Param("user_id") Integer user_id, @Param("identification") String identification);

    @Transactional
    @Modifying
    @Query("DELETE FROM FavouriteProduct od WHERE od.identification = :identification")
    void deleteByIdentification(@Param("identification") String identification);
}
