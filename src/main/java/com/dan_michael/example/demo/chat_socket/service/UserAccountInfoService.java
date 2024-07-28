package com.dan_michael.example.demo.chat_socket.service;


import com.dan_michael.example.demo.chat_socket.entities.Status;
import com.dan_michael.example.demo.chat_socket.entities.UserAccountInfo;
import com.dan_michael.example.demo.chat_socket.respository.UserAccountInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//@RequiredArgsConstructor
//public class UserTestService {
//
//    private final UserRepository repository;
//
//    public void saveUser(User userTest) {
//        userTest.setIs_active(1);
////        repository.save(userTest);
//    }
//
//    public void disconnect(User userTest) {
//        var storedUser = repository.findByEmail_(userTest.getEmail());
//        if (storedUser != null) {
//            storedUser.setIs_active(0);
//            repository.save(storedUser);
//        }
//    }
//
//    public List<User> findConnectedUsers() {
//        return repository.findAllByIs_active(1);
//    }
//}
@Service
@RequiredArgsConstructor
public class UserAccountInfoService {

    private final UserAccountInfoRepository repository;

    public void saveUser(UserAccountInfo user) {
        var check = repository.findUserTestByName(user.getNickName());
        user.setStatus(Status.ONLINE);
        if(check != null){
            check.setStatus(Status.ONLINE);
            repository.save(check);
            return;
        }
        repository.save(user);
    }

    public void disconnect(UserAccountInfo user) {
        var storedUser = repository.findUserTestByName(user.getNickName());
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }

    public List<UserAccountInfo> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }

    public UserAccountInfo findChatBot() {
        return repository.findUserTestByName("Chat Bot Support");
    }

}
