package com.dan_michael.example.demo.chat_socket.respository;

import com.dan_michael.example.demo.chat_socket.entities.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    @Query("SELECT pi FROM ChatMessage pi Where pi.chatId = :chatId ORDER BY pi.id ASC")
    List<ChatMessage> findByChatId(@Param("chatId")String chatId);
    @Query("SELECT pi FROM ChatMessage pi Where pi.id = :id")
    ChatMessage findById_(@Param("id")Integer id);
    @Transactional
    @Modifying
    @Query("DELETE FROM ChatMessage od WHERE od.id >= :id")
    void deleteById_(@Param("id") Integer id);
}
