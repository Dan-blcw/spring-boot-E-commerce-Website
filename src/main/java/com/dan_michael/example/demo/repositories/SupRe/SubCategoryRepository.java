package com.dan_michael.example.demo.repositories.SupRe;

import com.dan_michael.example.demo.model.entities.Material;
import com.dan_michael.example.demo.model.entities.SubEn.SubCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
    @Query("SELECT pi FROM SubCategory pi WHERE pi.identification = :identification")
    List<SubCategory> findBrandsByIAndIdentification(@Param("identification") String identification);
//    @Query("SELECT pi FROM SubCategory pi WHERE pi.status = 0")
//    List<Material> findByUnActive();
    @Transactional
    @Modifying
    @Query("DELETE FROM SubCategory  od WHERE  od.identification = :identification")
    void deleteByIdentification(@Param("identification") String identification);

    @Transactional
    @Modifying
    @Query("DELETE FROM SubCategory od WHERE  od.subCategoryName = :subCategoryName")
    void deleteBySubCategorysName(@Param("subCategoryName") String subCategoryName);
}
