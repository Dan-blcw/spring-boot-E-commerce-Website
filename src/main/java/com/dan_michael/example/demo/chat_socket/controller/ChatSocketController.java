package com.dan_michael.example.demo.chat_socket.controller;

import com.dan_michael.example.demo.chat_socket.entities.ChatImg;
import com.dan_michael.example.demo.chat_socket.entities.ChatMessage;
import com.dan_michael.example.demo.chat_socket.respository.ChatImgRepository;
import com.dan_michael.example.demo.chat_socket.service.ChatMessageService;
import com.dan_michael.example.demo.chat_socket.entities.ChatNotification;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.dtos.RequestMessageChatBotDtos;
import com.dan_michael.example.demo.chatbot.resository.QuestionAnswerRepository;
import com.dan_michael.example.demo.chatbot.service.ChatbotService;
import com.dan_michael.example.demo.model.entities.img.ProductImg;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.repositories.UserRepository;
import com.dan_michael.example.demo.repositories.image.UserImgRepository;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-box")
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatbotService chatBotService;
    private final UserRepository userRepository;
    private final ChatImgRepository chatImgRepository;
//    @MessageMapping("/chat")
//    public void processMessage(@Payload ChatMessage chatMessage) {
//        ChatMessage savedMsg = chatMessageService.save(chatMessage);
//        messagingTemplate.convertAndSendToUser(
//                chatMessage.getRecipientId(), "/queue/messages",
//                new ChatNotification(
//                        savedMsg.getId(),
//                        savedMsg.getSenderId(),
//                        savedMsg.getSenderImage(),
//                        savedMsg.getRecipientId(),
//                        savedMsg.getContent()
//                )
//        );
//        System.out.println(savedMsg);
//        var Chatbot = userRepository.findByName_(Constants.Chat_Bot_Name);
//        String recipientImage_chatbot= "";
//        if(Chatbot!=null){
//            recipientImage_chatbot= Chatbot.getUserImgUrl();
//        }
//        if(savedMsg.getRecipientId().equals(Constants.Chat_Bot_Name)){
//            var chatMessageResponse = ChatMessage.builder()
//                    .chatId(savedMsg.getChatId())
//                    .senderId(savedMsg.getRecipientId())
//                    .senderImage(recipientImage_chatbot)
//                    .recipientId(savedMsg.getSenderId())
//                    .content(
//                            getResponse(RequestMessageChatBotDtos.builder()
//                                    .message(savedMsg.getContent())
//                                    .build())
//                    )
//                    .build();
//            ChatMessage savedResponse = chatMessageService.save(chatMessageResponse);
//            System.out.println(savedResponse);
//            messagingTemplate.convertAndSendToUser(
//                    chatMessage.getRecipientId(), "/queue/messages",
//                    new ChatNotification(
//                            savedResponse.getId(),
//                            savedResponse.getSenderId(),
//                            savedResponse.getSenderImage(),
//                            savedResponse.getRecipientId(),
//                            savedResponse.getContent()
//                    )
//            );
//        }
//    }
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        switch (chatMessage.getAction()) {
            case "SEND":
                handleSendMessage(chatMessage);
                break;
            case "UPDATE":
                handleUpdateMessage(chatMessage);
                break;
            case "DELETE":
                handleDeleteMessage(chatMessage);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + chatMessage.getAction());
        }
    }

    private void handleSendMessage(ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getAction(),
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
                            savedMsg.getAction(),
                            savedResponse.getSenderId(),
                            savedResponse.getSenderImage(),
                            savedResponse.getRecipientId(),
                            savedResponse.getContent()
                    )
            );
        }
    }

    private void handleUpdateMessage(ChatMessage chatMessage) {
        ChatMessage updatedMsg = chatMessageService.update(chatMessage.getId(), chatMessage.getContent());
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        updatedMsg.getId(),
                        chatMessage.getAction(),
                        updatedMsg.getSenderId(),
                        updatedMsg.getSenderImage(),
                        updatedMsg.getRecipientId(),
                        updatedMsg.getContent()
                )
        );
        var Chatbot = userRepository.findByName_(Constants.Chat_Bot_Name);
        String recipientImage_chatbot= "";
        if(Chatbot!=null){
            recipientImage_chatbot= Chatbot.getUserImgUrl();
        }
        if(updatedMsg.getRecipientId().equals(Constants.Chat_Bot_Name)){
            var chatMessageResponse = ChatMessage.builder()
                    .id(updatedMsg.getId()+1)
                    .chatId(updatedMsg.getChatId())
                    .senderId(updatedMsg.getRecipientId())
                    .senderImage(recipientImage_chatbot)
                    .recipientId(updatedMsg.getSenderId())
                    .content(
                            getResponse(RequestMessageChatBotDtos.builder()
                                    .message(updatedMsg.getContent())
                                    .build())
                    )
                    .build();
            ChatMessage savedResponse = chatMessageService.save(chatMessageResponse);
            System.out.println(savedResponse);
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getRecipientId(), "/queue/messages",
                    new ChatNotification(
                            savedResponse.getId(),
                            updatedMsg.getAction(),
                            savedResponse.getSenderId(),
                            savedResponse.getSenderImage(),
                            savedResponse.getRecipientId(),
                            savedResponse.getContent()
                    )
            );
        }
    }

    private void handleDeleteMessage(ChatMessage chatMessage) {
        ChatMessage deletedMsg = chatMessageService.delete(chatMessage.getId());
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        deletedMsg.getId(),
                        chatMessage.getAction(),
                        deletedMsg.getSenderId(),
                        deletedMsg.getSenderImage(),
                        deletedMsg.getRecipientId(),
                        "This message was deleted"
                )
        );
        var Chatbot = userRepository.findByName_(Constants.Chat_Bot_Name);
        String recipientImage_chatbot= "";
        if(Chatbot!=null){
            recipientImage_chatbot= Chatbot.getUserImgUrl();
        }
        if(deletedMsg.getRecipientId().equals(Constants.Chat_Bot_Name)){
            ChatMessage deleteResponse = chatMessageService.delete(deletedMsg.getId()+1);
//            System.out.println(savedResponse);
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getRecipientId(), "/queue/messages",
                    new ChatNotification(
                            deleteResponse.getId(),
                            deleteResponse.getAction(),
                            deleteResponse.getSenderId(),
                            deleteResponse.getSenderImage(),
                            deleteResponse.getRecipientId(),
                            "This message was deleted"
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
