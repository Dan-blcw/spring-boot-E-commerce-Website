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
    @Query("SELECT pi FROM DetailSizeQuantity pi WHERE pi.identification = :identification AND  pi.identification_pro = :identification_pro")
    List<DetailSizeQuantity> findDetailSizeQuantityByIdentification(
            @Param("identification") String identification,
            @Param("identification_pro") String identification_pro
    );

    @Query("SELECT pi FROM DetailSizeQuantity pi WHERE pi.identification = :identification AND  pi.identification_pro = :identification_pro AND pi.size = :size")
    DetailSizeQuantity findDetailQuantity(
            @Param("size") String size,
            @Param("identification") String identification,
            @Param("identification_pro") String identification_pro
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM DetailSizeQuantity od WHERE od.identification = :identification and od.size = :size and od.identification_pro = :identification_pro")
    void deleteByIdentificationAndSizeName(
                    @Param("identification") String identification,
                    @Param("identification_pro") String identification_pro,
                    @Param("size") String size

    );


    @Query("SELECT od FROM DetailSizeQuantity od WHERE od.identification = :identification and od.size = :size and od.identification_pro = :identification_pro")
    DetailSizeQuantity DetailByIdentificationAndSizeName(
            @Param("identification") String identification,
            @Param("identification_pro") String identification_pro,
            @Param("size") String size);

    @Transactional
    @Modifying
    @Query("DELETE FROM DetailSizeQuantity od WHERE od.identification = :identification")
    void deleteByIdentification(@Param("identification") String identification);
}
