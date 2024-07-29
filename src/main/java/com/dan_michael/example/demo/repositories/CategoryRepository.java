package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.Product;
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

    @Query("SELECT pi FROM Category pi WHERE pi.name = :name")
    Category findCategoryByName_(@Param("name")String name);

}
