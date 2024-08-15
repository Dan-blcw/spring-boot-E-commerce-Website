package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);
    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Product findByName_(@Param("name")String name);

    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Product findByID_(@Param("id")Integer id);

    @Query("SELECT p FROM Product p " +
            "WHERE (:priceGte IS NULL OR p.finalPrice >= :priceGte) " +
            "AND (:priceLte IS NULL OR p.finalPrice <= :priceLte) " +
            "AND (:subCategoryName IS NULL OR p.subCategory like %:subCategoryName%) " +
            "AND (:categoryName IS NULL OR p.category like %:categoryName%) " +
            "AND (:productName IS NULL OR LOWER(FUNCTION('unaccent', p.name)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :productName, '%')))) " +
            "AND (:style IS NULL OR p.style like %:style%) " +
            "AND (:material IS NULL OR p.material like %:material%) " +
            "AND (:isReleased IS NULL OR p.newStatus = :isReleased) " +
            "AND (:isPromotion IS NULL OR p.saleStatus = :isPromotion) " +
            "AND (:ratingLt IS NULL OR p.rating < :ratingLt) " +
            "AND (:ratingGte IS NULL OR p.rating >= :ratingGte)")
    List<Product> search_all(
            @Param("productName") String productName,
            @Param("style") String style,
            @Param("material") String material,
            @Param("categoryName") String categoryName,
            @Param("subCategoryName") String subCategoryName,
            @Param("isPromotion") Boolean isPromotion,
            @Param("isReleased") Boolean isReleased,
            @Param("ratingGte") Integer ratingGte,
            @Param("ratingLt") Integer ratingLt,
            @Param("priceGte") Integer priceGte,
            @Param("priceLte") Integer priceLte);
    //            @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))")

    //            "AND (:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
    //            "AND (:productName IS NULL OR LOWER(FUNCTION('unaccent', p.name)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :productName, '%')))) " +
//            "AND (:productName IS NULL OR LOWER(FUNCTION('unaccent', CAST(p.name AS TEXT))) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :productName, '%')))) " +

    @Transactional
    @Modifying
    @Query("DELETE FROM Product od WHERE od.category = :category")
    void deleteByCategory(@Param("category") String category);


    @Transactional
    @Modifying
    @Query("DELETE FROM Product od WHERE od.subCategory = :subCategory")
    void deleteBySubCategorysName(@Param("subCategory") String subCategory);
}


