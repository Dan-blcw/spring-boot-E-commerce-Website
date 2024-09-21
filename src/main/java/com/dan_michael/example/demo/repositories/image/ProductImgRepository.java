package com.dan_michael.example.demo.repositories.image;

import com.dan_michael.example.demo.model.entities.img.ProductImg;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {
    @Transactional
    @Query("SELECT pi FROM ProductImg pi WHERE pi.identification = :identification_pro")
    List<ProductImg> findProductImgByProductName(@Param("identification_pro") String identification_pro);

    @Query("SELECT pi FROM ProductImg pi WHERE pi.identification = :identification_pro")
    ProductImg findProductImgByProductName_(@Param("identification_pro") String identification_pro);

    @Transactional
    @Query("SELECT pi FROM ProductImg pi WHERE pi.imageName = :imageName AND pi.identification = :productName")
    Optional<ProductImg> findProductImgByimageName(
            @Param("imageName") String imageName,
            @Param("productName") String productName
    );

    @Transactional
    @Query("SELECT pi FROM ProductImg pi WHERE pi.imageName = :imageName AND pi.identification = :productName")
    ProductImg findProductImgByimageName_(
            @Param("imageName") String imageName,
            @Param("productName") String productName
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductImg od WHERE od.identification = :identification and od.imageName = :imageName")
    void deleteByIdentificationAndImageName(
            @Param("identification") String identification,
            @Param("imageName") String imageName);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductImg od WHERE od.identification = :identification")
    void deleteByIdentification(@Param("identification") String identification);
}