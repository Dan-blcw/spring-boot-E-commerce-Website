package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.global.ChangeForgetPasswordDtos;
import com.dan_michael.example.demo.model.dto.global.ChangePasswordDtos;
import com.dan_michael.example.demo.model.dto.global.ChangeProfileDtos;
import com.dan_michael.example.demo.model.dto.global.ForgetPasswordDtos;
import com.dan_michael.example.demo.model.entities.User;
import com.dan_michael.example.demo.model.entities.img.UserImg;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.response.SubImgResponse;
import com.dan_michael.example.demo.repositories.image.UserImgRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository repository;

    private final UserImgRepository userImgRepository;

    public ResponseMessageDtos checkAccountForgetPassword(ForgetPasswordDtos request) {
        System.out.println(request);
        var user = repository.findByUserAndEmail(request.getName(),request.getEmail());
        if(user == null){
            return ResponseMessageDtos.builder()
                    .status(0)
                    .message(Constants.Verification_Fail)
                    .build();
        }
        return ResponseMessageDtos.builder()
                .status(1)
                .message(Constants.Verification_Success)
                .build();
    }

    public ResponseMessageDtos changeAccountForgetPassword(ChangeForgetPasswordDtos request) {
        var user = repository.findByEmail_(request.getEmail());
        if(user==null){
            return ResponseMessageDtos.builder()
                    .status(0)
                    .message(Constants.User_Not_Found)
                    .build();
        }
        if (!request.getNew_password().equals(request.getRenew_password())) {
            return ResponseMessageDtos.builder()
                    .status(0)
                    .message(Constants.Password_Not_Match)
                    .build();
        }
        user.setPassword(passwordEncoder.encode(request.getNew_password()));
        repository.save(user);
        return ResponseMessageDtos.builder()
                .status(1)
                .message(Constants.Change_FGPassword_Success)
                .build();
    }

    public SubImgResponse updateUserImage(MultipartFile file, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if(user == null){
            return null;
        }
        UserImg userImg = new UserImg();
        try {
            userImg.setImage(file.getBytes()); // Save image bytes
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
        userImg.setIdentification(user.getName()); // Set the product reference
        userImg.setImageName(file.getOriginalFilename());
        userImg.setImg_url(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(Constants.Global_Image_Path)
                .path(user.getName()+"/")
                .path(file.getOriginalFilename())
                .toUriString());
        userImgRepository.deleteByIdentification(user.getName());
        userImgRepository.save(userImg);
//        user.setUserImg(userImg);
        user.setUserImgUrl(userImg.getImg_url());
        repository.save(user);
        return SubImgResponse.builder()
                .id(userImg.getId())
                .identification(userImg.getIdentification())
                .imageName(userImg.getImageName())
                .img_url(userImg.getImg_url())
                .build();
    }
    public ResponseMessageDtos updateProfile(ChangeProfileDtos request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if(user ==null){
            return ResponseMessageDtos.builder()
                    .status(0)
                    .message(Constants.Change_InformationPF_Fail)
                    .build();
        }
        user.setName(request.getName());
        user.setUsername(request.getName());
//        user.setEmail(request.getEmail());

        user.setCompanyName(request.getCompanyName());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());

//        user.setIs_active(request.getIs_active());
        user.setLast_update(new Date());

        repository.save(user);
        return ResponseMessageDtos.builder()
                .status(1)
                .message(Constants.Change_InformationPF_Success)
                .build();
    }

    public ResponseMessageDtos updatePassword(ChangePasswordDtos request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getOld_password(), user.getPassword())) {
            return ResponseMessageDtos.builder()
                    .status(0)
                    .message(Constants.Wrong_Password)
                    .build();
        }
        if (!request.getNew_password().equals(request.getRenew_password())) {
            return ResponseMessageDtos.builder()
                    .status(0)
                    .message(Constants.Password_Not_Match)
                    .build();
        }
        user.setPassword(passwordEncoder.encode(request.getNew_password()));
        repository.save(user);
        return ResponseMessageDtos.builder()
                .status(1)
                .message(Constants.Change_Password)
                .build();
    }

}
