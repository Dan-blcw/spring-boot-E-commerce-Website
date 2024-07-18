package com.dan_michael.example.demo.controller;


import com.dan_michael.example.demo.model.dto.ChangePasswordDtos;
import com.dan_michael.example.demo.model.dto.ChangeProfileDtos;
import com.dan_michael.example.demo.model.dto.global.PaginationDto;
import com.dan_michael.example.demo.model.dto.ob.ProductListDtos;
import com.dan_michael.example.demo.service.ProductService;
import com.dan_michael.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/global")
@RequiredArgsConstructor
public class GuestController {

    private final ProductService service;

    private final UserService Change_service;
    @GetMapping(value = "/detail-ob")
    public ResponseEntity<?> detail(
            @RequestParam (required = false)Integer id

    ) {
        var ob = service.findbyID(id);
        return ResponseEntity.ok(ob);
    }
    @GetMapping(value = "/list-ob")
    public ResponseEntity<?> get_detail(
            @RequestParam (required = false)Integer _limit

    ) {
        var list = service.findAll();
        return ResponseEntity.ok(list);
    }
//--------------------Comment----------------------------------


//--------------------change information account----------------------------------
//    @PatchMapping("/change-profile")
//    public ResponseEntity<?> changeProfile(
//            @RequestBody ChangeProfileDtos request,
//            Principal connectedUser
//    ) {
//        Change_service.changeProfile(request, connectedUser);
//        return ResponseEntity.ok("changeProfile successfully !!!");
//    }
//
//    @PatchMapping("/change-password")
//    public ResponseEntity<?> changePassword(
//            @RequestBody ChangePasswordDtos request,
//            Principal connectedUser
//    ) {
//        var bol = Change_service.changePassword(request, connectedUser);
//        if(bol.equals("changePassword successfully !!!")){
//            return ResponseEntity.status(200).body("changePassword successfully !!!");
//        }else{
//            return ResponseEntity.status(405).body(bol);
//        }
//    }


}
