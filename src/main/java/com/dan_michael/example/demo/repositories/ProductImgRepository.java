package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {
}