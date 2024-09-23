package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.global.ChangeForgetPasswordDtos;
import com.dan_michael.example.demo.model.dto.global.ChangePasswordDtos;
import com.dan_michael.example.demo.model.dto.global.ChangeProfileDtos;
import com.dan_michael.example.demo.model.dto.global.ForgetPasswordDtos;
import com.dan_michael.example.demo.model.entities.OTP;
import com.dan_michael.example.demo.model.entities.User;
import com.dan_michael.example.demo.model.entities.img.UserImg;
import com.dan_michael.example.demo.model.response.DetailProfile;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.response.SubImgResponse;
import com.dan_michael.example.demo.repositories.OTPRepository;
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

    private final EmailSenderService emailService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository repository;

    private final OTPRepository otpRepository;

    private final UserImgRepository userImgRepository;

    public ResponseMessageDtos checkAccountForgetPassword(ForgetPasswordDtos request) {
        var user = repository.findByUserAndEmail(request.getEmail());
        if(user == null){
            return ResponseMessageDtos.builder()
                    .status(0)
                    .message(Constants.Verification_Fail)
                    .build();
        }
        if(otpRepository.findByOTPCodeByEmail(request.getEmail()) !=null){
            otpRepository.deleteByOTPEmail(request.getEmail());
        }
        OTP otp = OTP.builder()
                .OTPCode(ProductService.generateSku())
                .email(request.getEmail())
                .build();
        String toEmail = request.getEmail();
        String subject = Constants.Send_OTP_Change_Password;
        String logoPath = Constants.Logo_Path_0;
        otpRepository.save(otp);
        emailService.sendOtpForChangePassword(toEmail,subject,logoPath,otp.getOTPCode());
        return ResponseMessageDtos.builder()
                .status(200)
                .message(Constants.Verification_Success)
                .build();
    }

    public ResponseMessageDtos changeAccountForgetPassword(ChangeForgetPasswordDtos request) {
        var otpFlag = otpRepository.findByOTPCodeByOTP(request.getOTPCode());
        if(otpFlag == null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.OTP_CODE_Not_Found)
                    .build();
        }
        var user = repository.findByEmail_(otpFlag.getEmail());
        if(user==null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.User_Not_Found)
                    .build();
        }
        if (!request.getNew_password().equals(request.getRenew_password())) {
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Password_Not_Match)
                    .build();
        }
        user.setPassword(passwordEncoder.encode(request.getNew_password()));
        repository.save(user);
        return ResponseMessageDtos.builder()
                .status(200)
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
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setLast_update(new Date());
        repository.save(user);
        return ResponseMessageDtos.builder()
                .status(1)
                .message(Constants.Change_InformationPF_Success)
                .build();
    }

    public DetailProfile detailProfile(Principal connectedUser) {
        var save =  (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return DetailProfile.builder()
                .name(save.getName())
                .username(save.getUsername())
                .email(save.getEmail())
                .password(save.getPassword())
                .companyName(save.getCompanyName())
                .address(save.getAddress())
                .phoneNumber(save.getPhoneNumber())
                .date_joined(save.getDate_joined())
                .last_login(save.getLast_login())
                .last_update(save.getLast_update())
                .is_active(save.getIs_active())
                .useFirstDiscount(save.getUseFirstDiscount())
                .userImgUrl(save.getUserImgUrl())
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
