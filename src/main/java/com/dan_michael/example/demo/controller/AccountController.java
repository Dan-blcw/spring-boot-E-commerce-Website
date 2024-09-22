package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.ChangePasswordDtos;
import com.dan_michael.example.demo.model.dto.global.ChangeProfileDtos;
import com.dan_michael.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AccountController {

    private final UserService Change_service;

    @PatchMapping(value = "/update-user-image",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> updateUserImage(
            @ModelAttribute MultipartFile image,
            Principal connectedUser
    ) {
        return ResponseEntity.ok(Change_service.updateUserImage(image, connectedUser));
    }
    @PatchMapping(value = "/update-profile",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(
            @RequestBody ChangeProfileDtos request,
            Principal connectedUser
    ) {
         return ResponseEntity.ok(Change_service.updateProfile(request, connectedUser));
    }

    @GetMapping(value = "/detail-profile")
    public ResponseEntity<?> detailProfile(
            Principal connectedUser
    ) {
        return ResponseEntity.ok(Change_service.detailProfile(connectedUser));
    }

    @PatchMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestBody ChangePasswordDtos request,
            Principal connectedUser
    ) {
        return ResponseEntity.ok(Change_service.updatePassword(request, connectedUser));
    }

}
