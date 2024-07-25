package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.EmailSenderDtos;
import com.dan_michael.example.demo.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email-sender")
@RequiredArgsConstructor
public class EmailSenderController {

//    https://stackoverflow.com/questions/62302054/smtp-error-535-5-7-8-username-and-password-not-accepted-for-gmail-in-go
//    @GetMapping
//    public void createEmail() {
//        emailSenderService.setMailSender(
//                "phuhuong646@gmail.com",
//                "TestMailSender",
//                "Boday RIght here");
//    }
    private final EmailSenderService emailService;

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
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailSenderDtos request) {
        String toEmail = request.getEmail();
        String subject = request.getSubject();
        String name = request.getName();
        String body = request.getBody();
        String discountCode = request.getDiscountCode();
        String logoPath = "D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img.png";
        var response = emailService.sendEmail(toEmail, subject, name,body, logoPath, discountCode);

        return response;
    }


    @PostMapping("/get-discount-code")
    public String getDiscount(@RequestBody EmailSenderDtos request) {
        String toEmail = request.getEmail();
        String subject = request.getSubject();
        String name = request.getName();
        String discountCode = request.getDiscountCode();
        String logoPath = "D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img.png";
        var response = emailService.getDiscountCode(toEmail, subject, name, logoPath, discountCode);

        return response;
    }
}
