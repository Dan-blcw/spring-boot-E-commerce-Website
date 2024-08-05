package com.dan_michael.example.demo.chat_socket.service;


import com.dan_michael.example.demo.chat_socket.entities.ChatMessage;
import com.dan_michael.example.demo.chat_socket.entities.Status;
import com.dan_michael.example.demo.chat_socket.entities.UserAccountInfo;
import com.dan_michael.example.demo.chat_socket.respository.ChatMessageRepository;
import com.dan_michael.example.demo.chat_socket.respository.UserAccountInfoRepository;
import com.dan_michael.example.demo.chatbot.resository.QuestionAnswerRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserAccountInfoService {

    private final UserAccountInfoRepository repository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    public void saveUser(UserAccountInfo user) {
        var check = repository.findUserTestByName(user.getName());
        user.setStatus(Status.ONLINE);
        user.setImg_url(user.getImg_url());
        if(check != null){
            check.setStatus(Status.ONLINE);
            repository.save(check);
            return;
        }
        repository.save(user);
        if(user.getName() != Constants.Chat_Bot_Name){
            var Chatbot = userRepository.findByName_(Constants.Chat_Bot_Name);
            String recipientImage_chatbot= "";
            if(Chatbot!=null){
                recipientImage_chatbot= Chatbot.getUserImgUrl();
            }
            var chatId = chatRoomService
                    .getChatRoomId(Constants.Chat_Bot_Name, user.getName(), true)
                    .orElseThrow();
            ChatMessage chatMessage = ChatMessage.builder()
                        .chatId(chatId)
                        .senderId(Constants.Chat_Bot_Name)
                        .senderImage(recipientImage_chatbot)
                        .recipientId(user.getName())
                        .content("Chào bạn! \uD83D\uDC4B \nTôi là "+Constants.Chat_Bot_Name+", \nvà tôi ở đây để giúp bạn tìm sản phẩm hoặc giải đáp bất kỳ thắc mắc nào. Bạn cần tìm kiếm sản phẩm nào hay có câu hỏi gì về dịch vụ của chúng tôi? \nĐừng ngần ngại cho tôi biết, tôi sẵn sàng hỗ trợ bạn!")
                        .build();
            chatMessageRepository.save(chatMessage);

        }
    }

    public void disconnect(UserAccountInfo user) {
        var storedUser = repository.findUserTestByName(user.getName());
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }

    public List<UserAccountInfo> findConnectedUsers() {
//        return repository.findAllByStatus(Status.ONLINE);
        return repository.findAll_();
    }

    public UserAccountInfo findChatBot() {
        return repository.findUserTestByName("Chat Bot Support");
    }

}
