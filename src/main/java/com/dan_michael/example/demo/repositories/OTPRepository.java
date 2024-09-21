package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Discount;
import com.dan_michael.example.demo.model.entities.OTP;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OTPRepository extends JpaRepository<OTP, Integer> {
    @Query("SELECT pi FROM OTP pi WHERE pi.email = :email")
    OTP findByOTPCodeByEmail(@Param("email") String email);

    @Query("SELECT pi FROM OTP pi WHERE pi.OTPCode = :OTPCode")
    OTP findByOTPCodeByOTP(@Param("OTPCode") String OTPCode);

    @Transactional
    @Modifying
    @Query("DELETE FROM OTP od WHERE od.email = :email")
    void deleteByOTPEmail(@Param("email") String email);
}