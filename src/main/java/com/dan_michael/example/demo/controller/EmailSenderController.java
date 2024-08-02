package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.EmailSenderDtos;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.service.EmailSenderService;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email-sender")
@RequiredArgsConstructor
public class EmailSenderController {

    private final EmailSenderService emailService;
    @PostMapping("/answer-guest-quest")
    public ResponseMessageDtos answerGuest(@RequestBody EmailSenderDtos request) {
        String toEmail = request.getEmail();
        String subject = request.getSubject();
        String name = request.getName();
        String body = request.getBody();
        String discountCode = request.getDiscountCode();
        String logoPath = Constants.Logo_Path_0;
        return emailService.sendEmailAnswer(
                toEmail,
                subject,
                name,
                body,
                logoPath,
                discountCode
        );
    }
    @PostMapping("/get-discount-code")
    public ResponseMessageDtos getDiscount(@RequestBody EmailSenderDtos request) {
        String toEmail = request.getEmail();
        String subject = request.getSubject();
        String name = request.getName();
        String discountCode = request.getDiscountCode();
        String logoPath = Constants.Logo_Path_0;
        return emailService.getDiscountCode(
                toEmail,
                subject,
                name,
                logoPath,
                discountCode
        );
    }
}
//    https://stackoverflow.com/questions/62302054/smtp-error-535-5-7-8-username-and-password-not-accepted-for-gmail-in-go
//    @GetMapping
//    public void createEmail() {
//        emailSenderService.setMailSender(
//                "phuhuong646@gmail.com",
//                "TestMailSender",
//                "Boday RIght here");
//    }
//        @GetMapping("/send-email")
//        public String sendEmail(@RequestBody EmailSenderDtos request) {
//            String toEmail = request.getEmail();
//            String subject = request.getSubject();
//            String body = "Body of the email";
//            String logoPath = "D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img.png";
//            emailService.sendEmail(toEmail, subject, body, logoPath);
//
//            return "Email sent successfully!";
//        }