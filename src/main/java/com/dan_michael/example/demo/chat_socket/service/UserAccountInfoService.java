package com.dan_michael.example.demo.chat_socket.service;


import com.dan_michael.example.demo.chat_socket.entities.Status;
import com.dan_michael.example.demo.chat_socket.entities.UserAccountInfo;
import com.dan_michael.example.demo.chat_socket.respository.UserAccountInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserAccountInfoService {

    private final UserAccountInfoRepository repository;

    public void saveUser(UserAccountInfo user) {
        var check = repository.findUserTestByName(user.getName());
        user.setStatus(Status.ONLINE);
        if(check != null){
            check.setStatus(Status.ONLINE);
            repository.save(check);
            return;
        }
        repository.save(user);
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
