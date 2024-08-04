package com.dan_michael.example.demo.chat_socket.controller;

import com.dan_michael.example.demo.chat_socket.entities.ChatMessage;
import com.dan_michael.example.demo.chat_socket.service.ChatMessageService;
import com.dan_michael.example.demo.chat_socket.entities.ChatNotification;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.dtos.RequestMessageChatBotDtos;
import com.dan_michael.example.demo.chatbot.resository.QuestionAnswerRepository;
import com.dan_michael.example.demo.chatbot.service.ChatbotService;
import com.dan_michael.example.demo.repositories.UserRepository;
import com.dan_michael.example.demo.repositories.image.UserImgRepository;
import com.dan_michael.example.demo.util.Constants;
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
    private final UserRepository userRepository;
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getSenderImage(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
        System.out.println(savedMsg);
        var Chatbot = userRepository.findByName_(Constants.Chat_Bot_Name);
        String recipientImage_chatbot= "";
        if(Chatbot!=null){
            recipientImage_chatbot= Chatbot.getUserImgUrl();
        }
        if(savedMsg.getRecipientId().equals(Constants.Chat_Bot_Name)){
            var chatMessageResponse = ChatMessage.builder()
                    .chatId(savedMsg.getChatId())
                    .senderId(savedMsg.getRecipientId())
                    .senderImage(recipientImage_chatbot)
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
                            savedResponse.getSenderImage(),
                            savedResponse.getRecipientId(),
                            savedResponse.getContent()
                    )
            );
        }
    }

    public String getResponse(@Payload RequestMessageChatBotDtos message) {
        return chatBotService.handleInput(message.getMessage());
    }

    @GetMapping("/chat/{senderId}")
    public String getResponsec(@PathVariable String senderId) {
        return chatBotService.handleInput(senderId);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

}
