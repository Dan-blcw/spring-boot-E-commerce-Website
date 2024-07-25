package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.model.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);
    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Product findByName_(@Param("name")String name);
//    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :productId")
//    Product findProductWithImages(@Param("productId") Integer productId);
//    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.comments WHERE p.id = :productId")
//    Product findProductWithComments(@Param("productId") Integer productId);
//
//    // Optionally, you can fetch both images and comments
//    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images LEFT JOIN FETCH p.comments WHERE p.id = :productId")
//    Product findProductWithImagesAndComments(@Param("productId") Integer productId);
//    // Or fetch all products with their images and comments
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.images LEFT JOIN p.comments")
    List<Product> findAllProductsWithImagesAndComments();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :productId")
    Optional<Product> findByIdWithImages(@Param("productId") Integer productId);
}


