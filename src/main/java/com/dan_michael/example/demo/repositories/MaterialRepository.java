package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Material;
import com.dan_michael.example.demo.model.entities.TradeMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    @Query("SELECT pi FROM Material pi WHERE pi.name = :name")
    Material findByName(@Param("name") String name);
}