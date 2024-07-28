package com.dan_michael.example.demo.chat_socket.controller;

import com.dan_michael.example.demo.chat_socket.service.UserAccountInfoService;
import com.dan_michael.example.demo.chat_socket.entities.UserAccountInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserAccountInfoController {

    private final UserAccountInfoService userService;

    @MessageMapping("/addUser")
    @SendTo("/user/queue/public")
    public UserAccountInfo addUser(
            @Payload UserAccountInfo userTest
    ) throws IllegalArgumentException{
        userService.saveUser(userTest);
        return userTest;
    }
    @MessageMapping("/disconnectUser")
    @SendTo("/user/queue/public")
    public UserAccountInfo disconnectUser(
            @Payload UserAccountInfo userTest
    ) throws IllegalArgumentException {
        userService.disconnect(userTest);
        return userTest;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserAccountInfo>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

    @GetMapping("/chat-bot")
    public ResponseEntity<UserAccountInfo> findChatBot() {
        return ResponseEntity.ok(userService.findChatBot());
    }
}
