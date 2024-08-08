package com.dan_michael.example.demo.chat_socket.respository;

import com.dan_michael.example.demo.chat_socket.entities.Status;
import com.dan_michael.example.demo.chat_socket.entities.UserAccountInfo;
import com.dan_michael.example.demo.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface UserAccountInfoRepository extends JpaRepository<UserAccountInfo, String> {
    List<UserAccountInfo> findAllByStatus(Status status);
    @Query("SELECT pi FROM UserAccountInfo pi")
    List<UserAccountInfo> findAll_();

    @Query("SELECT pi FROM UserAccountInfo pi Where pi.role = :role AND pi.status = :status")
    List<UserAccountInfo> findAll_ByRole(@Param("role") Role role, @Param("status")Status status);
    @Query("SELECT pi FROM UserAccountInfo pi WHERE pi.name = :name")
    UserAccountInfo findUserTestByName(@Param("name")String name);
}
