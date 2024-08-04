package com.dan_michael.example.demo.chat_socket.service;

import com.dan_michael.example.demo.chat_socket.entities.ChatMessage;
import com.dan_michael.example.demo.chat_socket.respository.ChatMessageRepository;
import com.dan_michael.example.demo.repositories.image.UserImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final UserImgRepository userImgRepository;
    public ChatMessage save(ChatMessage chatMessage) {
//        var image_user = userImgRepository.findUserImgByUserName(chatMessage.getRecipientId());
//        if (image_user != null) {
//            chatMessage.setRecipientImage(image_user.getImg_url());
//        }else {
//            chatMessage.setRecipientImage("");
//        }
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        chatMessage.setRecipientImage(chatMessage.getRecipientImage());
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }

}
