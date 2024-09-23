package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Style;
import com.dan_michael.example.demo.model.entities.TradeMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StyleRepository extends JpaRepository<Style, Integer> {
    @Query("SELECT pi FROM Style pi WHERE pi.name = :name")
    Style findByName(@Param("name") String name);

    @Query("SELECT pi FROM Style pi WHERE pi.status = 0")
    List<Style> findByUnActive();

    @Query("SELECT pi FROM Style pi WHERE pi.status = 1")
    List<Style> findByActive();
}