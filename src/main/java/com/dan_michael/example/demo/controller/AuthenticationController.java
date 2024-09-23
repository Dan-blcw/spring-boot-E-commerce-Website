package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.AuthenticationDtos;
import com.dan_michael.example.demo.model.dto.global.CheckEmailValidate;
import com.dan_michael.example.demo.model.dto.global.RegisterDtos;
import com.dan_michael.example.demo.model.dto.global.SignInDtos;
import com.dan_michael.example.demo.service.AuthenticationService;
//import com.dan_michael.example.demo.service.LogoutService;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;


    @PostMapping("/email-validate")
    public ResponseEntity<?> CheckEmailValidate(
            @RequestBody CheckEmailValidate request
    ) {
        var response = service.CheckEmailValidate(request);
        if(response == null){
            return ResponseEntity.badRequest().body(Constants.Email_ARD_Exist);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register_new(
            @RequestBody RegisterDtos request
    ) {
        var response = service.register_new(request);
        if(response == null){
            return ResponseEntity.badRequest().body(Constants.Email_ARD_Exist);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDtos> login(
            @RequestBody SignInDtos request
    ) {
        return ResponseEntity.ok(service.Login(request));
    }
}