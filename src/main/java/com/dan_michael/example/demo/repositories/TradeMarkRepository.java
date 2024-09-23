package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Cart;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.Style;
import com.dan_michael.example.demo.model.entities.TradeMark;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TradeMarkRepository extends JpaRepository<TradeMark, Integer> {
    @Query("SELECT pi FROM TradeMark pi WHERE pi.name = :name")
    TradeMark findByName(@Param("name") String name);

    @Query("SELECT pi FROM TradeMark pi WHERE pi.status = 0")
    TradeMark findByUnActive();
    @Query("SELECT pi FROM TradeMark pi WHERE pi.status = 1")
    List<TradeMark> findByActive();

}