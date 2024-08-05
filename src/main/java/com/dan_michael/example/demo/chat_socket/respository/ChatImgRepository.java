package com.dan_michael.example.demo.chat_socket.respository;

import com.dan_michael.example.demo.chat_socket.entities.ChatImg;
import com.dan_michael.example.demo.model.entities.img.ProductImg;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatImgRepository extends JpaRepository<ChatImg, Integer> {
    @Query("SELECT pi FROM ChatImg pi WHERE pi.img_url = :img_url")
    ChatImg findChatImgByUrl(@Param("img_url") String img_url);

}