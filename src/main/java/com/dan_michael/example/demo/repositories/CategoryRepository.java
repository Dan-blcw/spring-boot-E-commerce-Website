package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryByName(String name);

    Category findCategoryByName_(String name);

}
