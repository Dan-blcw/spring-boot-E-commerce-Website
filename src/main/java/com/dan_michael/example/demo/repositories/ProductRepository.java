package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.model.entities.SubEn.Brands;
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

    @Query("SELECT p FROM Product p WHERE  p.brand = :brands ")
    List<Product> find_brand(@Param("brands") String brands);
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Product findByID_(@Param("id")Integer id);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.images LEFT JOIN p.comments")
    List<Product> findAllProductsWithImagesAndComments();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :productId")
    Optional<Product> findByIdWithImages(@Param("productId") Integer productId);


//    @Query("SELECT p FROM Product p " +
//            "WHERE (:priceGte IS NULL OR p.finalPrice >= :priceGte) " +
//            "AND (:priceLte IS NULL OR p.finalPrice <= :priceLte) " +
//            "AND (:brand IS NULL OR p.brand IN :brand) " +
//            "AND (:isReleased IS NULL OR p.newStatus = :isReleased) " +
//            "AND (:isPromotion IS NULL OR p.saleStatus = :isPromotion) " +
//            "AND (:ratingLt IS NULL OR p.rating < :ratingLt) " +
//            "AND (:ratingGte IS NULL OR p.rating >= :ratingGte)")
//    List<Product> search_all(@Param("brand") String brand,
//                             @Param("isPromotion") Boolean isPromotion,
//                             @Param("isReleased") Boolean isReleased,
//                             @Param("ratingGte") Integer ratingGte,
//                             @Param("ratingLt") Integer ratingLt,
//                             @Param("priceGte") Integer priceGte,
//                             @Param("priceLte") Integer priceLte);
//@Query("SELECT p FROM Product p " +
//        "WHERE (:priceGte IS NULL OR p.finalPrice >= :priceGte) " +
//        "AND (:priceLte IS NULL OR p.finalPrice <= :priceLte) " +
//        "AND (:brands IS NULL OR p.brand IN :brands) " +
//        "AND (:isReleased IS NULL OR p.newStatus = :isReleased) " +
//        "AND (:isPromotion IS NULL OR p.saleStatus = :isPromotion) " +
//        "AND (:ratingLt IS NULL OR p.rating < :ratingLt) " +
//        "AND (:ratingGte IS NULL OR p.rating >= :ratingGte) " +
//        "ORDER BY " +
//        "CASE WHEN :sort LIKE 'ASC' THEN p.finalPrice END ASC, " +
//        "CASE WHEN :sort LIKE 'DESC' THEN p.finalPrice END DESC ")
//List<Product> search_all(@Param("brands") String brands,
//                         @Param("isPromotion") Boolean isPromotion,
//                         @Param("isReleased") Boolean isReleased,
//                         @Param("ratingGte") Integer ratingGte,
//                         @Param("ratingLt") Integer ratingLt,
//                         @Param("priceGte") Integer priceGte,
//                         @Param("priceLte") Integer priceLte,
//                         @Param("sort") String sort);

    @Query("SELECT p FROM Product p " +
            "WHERE (:priceGte IS NULL OR p.finalPrice >= :priceGte) " +
            "AND (:priceLte IS NULL OR p.finalPrice <= :priceLte) " +
            "AND (:brands IS NULL OR p.brand IN :brands) " +
            "AND (:isReleased IS NULL OR p.newStatus = :isReleased) " +
            "AND (:isPromotion IS NULL OR p.saleStatus = :isPromotion) " +
            "AND (:ratingLt IS NULL OR p.rating < :ratingLt) " +
            "AND (:ratingGte IS NULL OR p.rating >= :ratingGte) ")
    List<Product> search_all(@Param("brands") String brands,
                             @Param("isPromotion") Boolean isPromotion,
                             @Param("isReleased") Boolean isReleased,
                             @Param("ratingGte") Integer ratingGte,
                             @Param("ratingLt") Integer ratingLt,
                             @Param("priceGte") Integer priceGte,
                             @Param("priceLte") Integer priceLte);


}


