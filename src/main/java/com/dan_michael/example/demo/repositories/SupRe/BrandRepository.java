package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.SubEn.Brand;
import com.dan_michael.example.demo.model.entities.SubEn.QuantityDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query("SELECT pi FROM Brand pi WHERE pi.identification = :identification")
    List<Brand> findBrandsByIAndIdentification(@Param("identification") String identification);

    @Transactional
    @Modifying
    @Query("DELETE FROM Brand od WHERE  od.identification = :identification")
    void deleteByIdentification(@Param("identification") String identification);
}
