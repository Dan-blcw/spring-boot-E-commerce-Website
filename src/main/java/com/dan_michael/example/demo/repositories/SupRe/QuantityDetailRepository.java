package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.SubEn.Brands;
import com.dan_michael.example.demo.model.entities.SubEn.QuantityDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuantityDetailRepository extends JpaRepository<QuantityDetail, Integer> {
    @Query("SELECT pi FROM QuantityDetail pi WHERE pi.identification = :identification")
    List<QuantityDetail> findQuantityDetailsByIAndIdentification(@Param("identification") String identification);

//    @Query("SELECT pi FROM QuantityDetail pi WHERE pi.id = :id")
//    List<QuantityDetail> findQuantityDetailsByID_(@Param("id") Integer id);
}
