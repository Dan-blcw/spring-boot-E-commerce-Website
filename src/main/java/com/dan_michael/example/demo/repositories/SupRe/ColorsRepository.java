package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.SubEn.Brands;
import com.dan_michael.example.demo.model.entities.SubEn.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ColorsRepository extends JpaRepository<Colors, Integer> {

    @Query("SELECT pi FROM Colors pi WHERE pi.identification = :identification")
    List<Colors> findColorsByIAndIdentification(@Param("identification") String identification);

}
