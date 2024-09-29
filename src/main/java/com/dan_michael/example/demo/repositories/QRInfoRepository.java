package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.OTP;
import com.dan_michael.example.demo.model.entities.QRInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QRInfoRepository extends JpaRepository<QRInfo, Integer> {
    @Query("SELECT pi FROM QRInfo pi WHERE pi.status = 1")
    List<QRInfo> findQRInfoActive();

    @Query("SELECT pi FROM QRInfo pi WHERE pi.accountNo = :accountNo AND pi.accountName = :accountName")
    QRInfo findQRAccount(@Param("accountNo") String accountNo, @Param("accountName") String accountName);

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM QRInfo od WHERE od.email = :email")
//    void deleteByOTPEmail(@Param("email") String email);
}