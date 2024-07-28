package com.dan_michael.example.demo.chat_socket.respository;

import com.dan_michael.example.demo.chat_socket.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);
}
