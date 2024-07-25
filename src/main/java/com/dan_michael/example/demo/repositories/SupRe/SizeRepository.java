package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.SubEn.Brands;
import com.dan_michael.example.demo.model.entities.SubEn.Sizes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SizeRepository extends JpaRepository<Sizes, Integer> {
    @Query("SELECT pi FROM Sizes pi WHERE pi.identification = :identification")
    List<Sizes> findSizesByIAndIdentification(@Param("identification") String identification);
}
