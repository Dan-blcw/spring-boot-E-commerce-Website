package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.SubEn.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brands, Integer> {
    @Query("SELECT pi FROM Brands pi WHERE pi.identification = :identification")
    List<Brands> findBrandsByIAndIdentification(@Param("identification") String identification);


    @Query("SELECT pi FROM Brands pi WHERE pi.brand = :identification")
    List<Brands> findBrandsByListBrands(@Param("identification") String identification);

}
