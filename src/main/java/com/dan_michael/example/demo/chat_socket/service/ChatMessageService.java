package com.dan_michael.example.demo.chat_socket.service;

import com.dan_michael.example.demo.chat_socket.entities.ChatMessage;
import com.dan_michael.example.demo.chat_socket.respository.ChatMessageRepository;
import com.dan_michael.example.demo.chatbot.entities.dtos.RequestMessageChatBotDtos;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.repositories.image.UserImgRepository;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final UserImgRepository userImgRepository;
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        chatMessage.setSenderImage(chatMessage.getSenderImage());
        repository.save(chatMessage);
        return chatMessage;
    }


    public ChatMessage delete(Integer id) {
        var chatMessage = repository.findById_(id);
        if(
                chatMessage !=null &&
                !Objects.equals(chatMessage.getContent(), chatMessage.getSenderId()+" đã thu hồi một tin nhắn") &&
                !Objects.equals(chatMessage.getSenderId(), Constants.Chat_Bot_Name)
        ){
            chatMessage.setContent(chatMessage.getSenderId()+" đã thu hồi một tin nhắn");
            chatMessage.setAction("DELETE");
            return repository.save(chatMessage);
        }else if(
            Objects.equals(chatMessage.getContent(), chatMessage.getSenderId()+" đã thu hồi một tin nhắn") ||
            Objects.equals(chatMessage.getSenderId(), Constants.Chat_Bot_Name)
        ){
            repository.deleteById(id);
        }
        return chatMessage;
    }

    public ChatMessage update(Integer id, String reContent) {
        var chatMessage = repository.findById_(id);
        if(chatMessage != null){
            chatMessage.setContent(reContent);
            chatMessage.setAction("UPDATE");
            return repository.save(chatMessage);
        }
        return null;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
//    nếu không dùng send trực tiếp thì dùng này
//    public ResponseMessageDtos update(Integer id, String reContent) {
//        var chatMessage = repository.findById_(id);
//        if(chatMessage != null){
//            chatMessage.setContent(reContent);
//            repository.save(chatMessage);
//            return ResponseMessageDtos.builder()
//                    .status(200)
//                    .message("Update Message Successfully !!!")
//                    .build();
//        }
//        return ResponseMessageDtos.builder()
//                .status(400)
//                .message("Update Message Failure !!!")
//                .build();
//    }
//
//public ResponseMessageDtos delete(Integer id) {
//    var chatMessage = repository.findById_(id);
//    if(chatMessage !=null && chatMessage.getContent() != "Bạn đã thu hồi một tin nhắn"){
//        chatMessage.setContent("Bạn đã thu hồi một tin nhắn");
//        repository.save(chatMessage);
//        return ResponseMessageDtos.builder()
//                .status(200)
//                .message("Delete Message Successfully !!!")
//                .build();
//    }else if(chatMessage.getContent() == "Bạn đã thu hồi một tin nhắn"){
//        repository.deleteById(id);
//    }
//    return ResponseMessageDtos.builder()
//            .status(400)
//            .message("Delete Message Failure !!!")
//            .build();
//}
}
