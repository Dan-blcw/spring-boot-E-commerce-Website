package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.ChangePasswordDtos;
import com.dan_michael.example.demo.model.dto.global.ChangeProfileDtos;
import com.dan_michael.example.demo.service.ProductService;
import com.dan_michael.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AccountController {

    private final UserService Change_service;

    private final ProductService service;

    @PatchMapping("/change-profile")
    public ResponseEntity<?> changeProfile(
            @RequestBody ChangeProfileDtos request,
            Principal connectedUser
    ) {
         Change_service.changeProfile(request, connectedUser);
         return ResponseEntity.ok("changeProfile successfully !!!");
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordDtos request,
            Principal connectedUser
    ) {
        var bol = Change_service.changePassword(request, connectedUser);
        if(bol.equals("changePassword successfully !!!")){
            return ResponseEntity.status(200).body("changePassword successfully !!!");
        }else{
            return ResponseEntity.status(405).body(bol);
        }
    }

}
