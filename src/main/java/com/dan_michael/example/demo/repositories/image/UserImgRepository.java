package com.dan_michael.example.demo.repositories.image;

import com.dan_michael.example.demo.model.entities.img.UserImg;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserImgRepository extends JpaRepository<UserImg, Integer> {
    @Transactional
    @Query("SELECT pi FROM UserImg pi WHERE pi.identification = :identification_pro")
    UserImg findUserImgByUserName(@Param("identification_pro") String identification_pro);

    @Transactional
    @Query("SELECT pi FROM UserImg pi WHERE pi.imageName = :imageName AND pi.identification = :name")
    Optional<UserImg> findUserImgByimageName(
            @Param("imageName") String imageName,
            @Param("name") String name
    );
}