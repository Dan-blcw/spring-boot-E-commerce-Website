package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.TradeMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryByName(String name);


    @Query("SELECT pi FROM Category pi ")
    List<Category> findAll_();
    @Query("SELECT pi FROM Category pi WHERE pi.id = :id")
    Category findCategoryById_(@Param("id") Integer id);

    @Query("SELECT pi FROM Category pi WHERE pi.name = :name ")
    Category findCategoryByName_(@Param("name")String name);
    @Query("SELECT pi FROM Category pi WHERE pi.name = :name OR pi.sku  = :sku")
    Category findCategoryByName_Sku(@Param("name")String name,@Param("sku")String sku);
    @Query("SELECT pi FROM Category pi WHERE pi.status = 0")
    List<Category> findByUnActive();

    @Query("SELECT pi FROM Category pi WHERE pi.status = 1")
    List<Category>  findByActive();
}
