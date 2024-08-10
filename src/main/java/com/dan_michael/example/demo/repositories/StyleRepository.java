package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Style;
import com.dan_michael.example.demo.model.entities.TradeMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StyleRepository extends JpaRepository<Style, Integer> {
    @Query("SELECT pi FROM Style pi WHERE pi.name = :name")
    Style findByName(@Param("name") String name);
}