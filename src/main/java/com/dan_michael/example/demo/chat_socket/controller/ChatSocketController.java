package com.dan_michael.example.demo.chat_socket.controller;

import com.dan_michael.example.demo.chat_socket.entities.ChatMessage;
import com.dan_michael.example.demo.chat_socket.service.ChatMessageService;
import com.dan_michael.example.demo.chat_socket.entities.ChatNotification;
import com.dan_michael.example.demo.chatbot.entities.RequestMessageChatBotDtos;
import com.dan_michael.example.demo.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-box")
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatbotService chatBotService;
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        System.out.println(savedMsg);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
        if(savedMsg.getRecipientId().equals("Chat Bot Support")){
            var chatMessageResponse = ChatMessage.builder()
                    .chatId(savedMsg.getChatId())
                    .senderId(savedMsg.getRecipientId())
                    .recipientId(savedMsg.getSenderId())
                    .content(
                            getResponse(RequestMessageChatBotDtos.builder()
                                    .message(savedMsg.getContent())
                                    .build())
                    )
                    .build();
            ChatMessage savedResponse = chatMessageService.save(chatMessageResponse);
            System.out.println(savedResponse);
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getRecipientId(), "/queue/messages",
                    new ChatNotification(
                            savedResponse.getId(),
                            savedResponse.getSenderId(),
                            savedResponse.getRecipientId(),
                            savedResponse.getContent()
                    )
            );
        }
    }

    @GetMapping("/chat-bot")
    public String getResponse(@Payload RequestMessageChatBotDtos message) {
        return chatBotService.handleInput(message);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
