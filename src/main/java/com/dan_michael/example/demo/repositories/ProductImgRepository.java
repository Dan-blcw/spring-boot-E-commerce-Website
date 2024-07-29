package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {
    @Transactional
    @Query("SELECT pi FROM ProductImg pi WHERE pi.identification = :identification_pro")
    List<ProductImg> findProductImgByProductId(@Param("identification_pro") String identification_pro);

    @Transactional
    @Query("SELECT pi FROM ProductImg pi WHERE pi.imageName = :imageName AND pi.identification = :productName")
    Optional<ProductImg> findProductImgByimageName(
            @Param("imageName") String imageName,
            @Param("productName") String productName
    );
}