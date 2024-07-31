package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.SubEn.QuantityDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuantityDetailRepository extends JpaRepository<QuantityDetail, Integer> {
    @Query("SELECT pi FROM QuantityDetail pi WHERE pi.identification = :identification")
    List<QuantityDetail> findQuantityDetailsByIAndIdentification(@Param("identification") String identification);

    @Query("SELECT pi FROM QuantityDetail pi WHERE pi.identification = :identification and pi.color = :color")
    QuantityDetail findQuantityDetailsByIAndIdentificationAndColor(
            @Param("identification") String identification,
            @Param("color") String color);
    @Transactional
    @Modifying
    @Query("DELETE FROM QuantityDetail od WHERE od.identification = :identification and od.color = :color")
    void deleteByIdentificationAndColor(
            @Param("identification") String identification,
            @Param("color") String color);

    @Transactional
    @Modifying
    @Query("DELETE FROM QuantityDetail od WHERE od.identification = :identification")
    void deleteByIdentification(@Param("identification") String identification);
}
