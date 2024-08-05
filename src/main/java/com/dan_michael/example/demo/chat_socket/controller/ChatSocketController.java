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
//    @MessageMapping("/chat")
//    public void processMessage(
//            @RequestParam(value = "file", required = false) MultipartFile file,
//            @Payload ChatMessage chatMessage) {
//        ChatMessage savedMsg ;
//        // Handle the file if it exists
//        if (file != null && !file.isEmpty()) {
//            // Save the file to your desired location and update chatMessage with the file path or URL
////            String filePath = saveFile(file); // Implement this method to save the file and return its path or URL
////            chatMessage.setFile(file);
//            ChatImg productImg = new ChatImg();
//            try {
//                productImg.setImage(file.getBytes()); // Save image bytes
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle exception
//            }
//            productImg.setIdentification(chatMessage.getSenderId()); // Set the product reference
//            productImg.setImageName(file.getOriginalFilename());
//            productImg.setImg_url(ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path(Constants.Global_Image_Path)
//                    .path(chatMessage.getSenderId()+"/")
//                    .path(file.getOriginalFilename())
//                    .toUriString());
//            chatImgRepository.save(productImg);
//            chatMessage.setSenderImage(productImg.getImg_url());
//        }
//        savedMsg = chatMessageService.save(chatMessage);
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
//
//        var Chatbot = userRepository.findByName_(Constants.Chat_Bot_Name);
//        String recipientImage_chatbot = "";
//        if (Chatbot != null) {
//            recipientImage_chatbot = Chatbot.getUserImgUrl();
//        }
//
//        if (savedMsg.getRecipientId().equals(Constants.Chat_Bot_Name)) {
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
