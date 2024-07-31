package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.SubEn.DetailSizeQuantity;
import com.dan_michael.example.demo.model.entities.SubEn.FavouriteProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailSizeQuantityRepository extends JpaRepository<DetailSizeQuantity, Integer> {
    @Query("SELECT pi FROM DetailSizeQuantity pi WHERE pi.identification = :identification")
    List<DetailSizeQuantity> findDetailSizeQuantityByIdentification(@Param("identification") String identification);

    @Transactional
    @Modifying
    @Query("DELETE FROM DetailSizeQuantity od WHERE od.identification = :identification and od.size = :size")
    void deleteByIdentificationAndSizeName(
                    @Param("identification") String identification,
                    @Param("size") String size);


    @Query("SELECT od FROM DetailSizeQuantity od WHERE od.identification = :identification and od.size = :size")
    DetailSizeQuantity DetailByIdentificationAndSizeName(
            @Param("identification") String identification,
            @Param("size") String size);

    @Transactional
    @Modifying
    @Query("DELETE FROM DetailSizeQuantity od WHERE od.identification = :identification")
    void deleteByIdentification(@Param("identification") String identification);
}
