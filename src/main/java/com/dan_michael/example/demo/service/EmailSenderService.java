package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.entities.Discount;
import com.dan_michael.example.demo.model.entities.User;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.repositories.DiscountRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import com.dan_michael.example.demo.util.Constants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    private final UserRepository userRepository;

    private final DiscountRepository discountRepository;
//---------------------------------Done-------------------------------------------------------------------
    public ResponseMessageDtos sendEmailAnswer(String toEmail, String subject, String name, String question, String answer, String logoPath) {
        MimeMessage message = mailSender.createMimeMessage();
        var user = userRepository.findByEmail_(toEmail);
        if(user == null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.User_Not_Found)
                    .build();
        }
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Constants.Email_Of_Company);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            String htmlBody = "<div style='text-align: center; font-family: Arial, sans-serif; font-size: 15px;'>"
                    + "<img src='cid:image_logo' style='display: block; margin: 0 auto; max-width: 360px; border-radius: 50%;' />"
                    + "<h1>Hi " + name + "!</h1>"
                    + "<p>Thank you for reaching out to us with your question!</p>"
                    + "<p>We truly appreciate your interest and value the opportunity to assist you.</p>"
                    + "<p><strong>Your Question:</strong></p>"
                    + "<p style='font-style: italic;'>" + question + "</p>"
                    + "<p><strong>Our Answer:</strong></p>"
                    + "<p>" + answer + "</p>"
                    + "<p>If you have any further questions or need additional assistance, please don't hesitate to reach out to us.</p>"
                    + "<p>We are here to help and ensure you have the best experience possible.</p>"

                    + "<div>"
                    + "<a href='http://www.facebook.com'><img src='cid:image_fb' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<a href='http://www.linkedin.com'><img src='cid:image_li' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<a href='http://www.youtube.com'><img src='cid:image_yt' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<a href='http://www.pinterest.com'><img src='cid:image_pin' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "</div>"
                    + "<p>Best regards,</p>"
                    + "<p>"+Constants.Team_Name +"</p>"
                    + "</div>";


            helper.setText(htmlBody, true);

            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File logo = new File(Objects.requireNonNull(classLoader.getResource(logoPath)).getFile());
            helper.addInline("image_logo", logo);


            File fbIcon = new File(Objects.requireNonNull(classLoader.getResource(Constants.Logo_Path_1)).getFile());
            File liIcon = new File(Objects.requireNonNull(classLoader.getResource(Constants.Logo_Path_2)).getFile());
            File ytIcon = new File(Objects.requireNonNull(classLoader.getResource(Constants.Logo_Path_3)).getFile());
            File pinIcon = new File(Objects.requireNonNull(classLoader.getResource(Constants.Logo_Path_4)).getFile());

            helper.addInline("image_fb", fbIcon);
            helper.addInline("image_li", liIcon);
            helper.addInline("image_yt", ytIcon);
            helper.addInline("image_pin", pinIcon);

            mailSender.send(message);
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Send_Mail_Answer_Success)
                    .build();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseMessageDtos.builder()
                .status(404)
                .message(Constants.Send_Mail_Answer_Fail)
                .build();
    }
    public ResponseMessageDtos getDiscountCode(String toEmail, String subject, String logoPath) {
        MimeMessage message = mailSender.createMimeMessage();
        var user = userRepository.findByEmail_(toEmail);
        if(user == null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.User_Not_Found)
                    .build();
        }
        if(user.getUseFirstDiscount() == true){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Already_Use_First_Discount)
                    .build();
        }
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Constants.Email_Of_Company);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            var sku = generateSku();
            Discount saveDis = Discount.builder()
                    .sku(sku)
                    .status(1)
                    .percentDiscount(10)
                    .createDate(new Date())
                    .build();
            String htmlBody = "<div style='text-align: center; font-family: Arial, sans-serif; font-size: 15px;'>"
                    + "<img src='cid:image_logo' style='display: block; margin: 0 auto; max-width: 360px; border-radius: 50%;' />"
                    + "<h1>Hi "+ user.getName()+ "!</h1>"
                    + "<p>Thanks for signing up with our E-commerce platform! You're about to join the largest online community of enthusiastic shoppers.</p>"
                    + "<p>As a welcome gift, enjoy a discount on your first order!</p>"
                    + "<p>Grab the Discount Code and return to our site and enter the discount code by clicking the link below</p>"
                    + "<p><a href='https://www.pinterest.com/pin/710020697515286358/'style='display: inline-block; margin: 20px auto; font-size: 32px; padding: 10px 20px; color: #000; background-color: #DDD6B9; text-decoration: none; border-radius: 5px;'> "+sku+ "</a></p>"
                    + "<p>All first orders will receive a 20% discount for all customers. Also.We have many special offers for students, women, Teens, tech lovers, etc.</p>"
                    + "<p> Check out our special offers page to see if you're eligible for any type of discount</p>"
                    + "<p>Here’s to your success,</p>"
                    + "<div>"
                    + "<a href='http://www.facebook.com'><img src='cid:image_fb' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<a href='http://www.linkedin.com'><img src='cid:image_li' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<a href='http://www.youtube.com'><img src='cid:image_yt' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<a href='http://www.pinterest.com'><img src='cid:image_pin' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<p>The E-commerce Team (Dan - Michael)</p>"
                    + "</div>"
                    + "</div>";

            helper.setText(htmlBody, true);

            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File logo = new File(Objects.requireNonNull(classLoader.getResource(logoPath)).getFile());
            helper.addInline("image_logo", logo);


            File fbIcon = new File(Objects.requireNonNull(classLoader.getResource(Constants.Logo_Path_1)).getFile());
            File liIcon = new File(Objects.requireNonNull(classLoader.getResource(Constants.Logo_Path_2)).getFile());
            File ytIcon = new File(Objects.requireNonNull(classLoader.getResource(Constants.Logo_Path_3)).getFile());
            File pinIcon = new File(Objects.requireNonNull(classLoader.getResource(Constants.Logo_Path_4)).getFile());

            helper.addInline("image_fb", fbIcon);
            helper.addInline("image_li", liIcon);
            helper.addInline("image_yt", ytIcon);
            helper.addInline("image_pin", pinIcon);

            discountRepository.save(saveDis);
            mailSender.send(message);

            user.setUseFirstDiscount(true);
            userRepository.save(user);
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Send_Mail_Get_Discount_Success)
                    .build();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseMessageDtos.builder()
                .status(404)
                .message(Constants.Send_Mail_Get_Discount_Fail)
                .build();
    }

    public ResponseMessageDtos sendOtpForChangePassword(String toEmail, String subject, String logoPath,String otp) {
        MimeMessage message = mailSender.createMimeMessage();
        var user = userRepository.findByEmail_(toEmail);
        if (user == null) {
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.User_Not_Found)
                    .build();
        }
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Constants.Email_Of_Company);
            helper.setTo(toEmail);
            helper.setSubject(subject);


            // Nội dung email về OTP
            String htmlBody = "<div style='text-align: center; font-family: Arial, sans-serif; font-size: 15px;'>"
                    + "<img src='cid:image_logo' style='display: block; margin: 0 auto; max-width: 360px; border-radius: 50%;' />"
                    + "<h1>Hello, " + user.getName() + "!</h1>"
                    + "<p>You have requested to reset your password for your account.</p>"
                    + "<p>Your OTP code is:</p>"
                    + "<p><strong style='font-size: 24px;'>" + otp + "</strong></p>"
                    + "<p>Please enter this OTP code to proceed with resetting your password.</p>"
                    + "<p>Note: This OTP is valid for 10 minutes only.</p>"
                    + "<p>If you did not request this, please ignore this email.</p>"
                    + "<p>Best regards,</p>"
                    + "<p>The E-commerce Support Team</p>"
                    + "</div>";
            helper.setText(htmlBody, true);

            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File logo = new File(Objects.requireNonNull(classLoader.getResource(logoPath)).getFile());
            helper.addInline("image_logo", logo);


            // Gửi email
            mailSender.send(message);

            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Send_Otp_Success)
                    .build();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseMessageDtos.builder()
                .status(404)
                .message(Constants.Send_Otp_Fail)
                .build();
    }
    public ResponseMessageDtos sendOtpForAccountRegistration(String toEmail, String subject, String logoPath, String otp) {
        MimeMessage message = mailSender.createMimeMessage();
        var user = userRepository.findByEmail_(toEmail);
        if (user != null) {
            return ResponseMessageDtos.builder()
                    .status(400)
                    .message(Constants.User_Already_Exists)
                    .build();
        }
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Constants.Email_Of_Company);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // Nội dung email về OTP cho đăng ký tài khoản
            String htmlBody = "<div style='text-align: center; font-family: Arial, sans-serif; font-size: 15px;'>"
                    + "<img src='cid:image_logo' style='display: block; margin: 0 auto; max-width: 360px; border-radius: 50%;' />"
                    + "<h1>Welcome to Our Service!</h1>"
                    + "<p>Thank you for registering for an account with us.</p>"
                    + "<p>Your OTP code for completing the registration process is:</p>"
                    + "<p><strong style='font-size: 24px;'>" + otp + "</strong></p>"
                    + "<p>Please enter this OTP code to verify your email address and complete the registration.</p>"
                    + "<p>Note: This OTP is valid for 10 minutes only.</p>"
                    + "<p>If you did not initiate this request, please ignore this email.</p>"
                    + "<p>Best regards,</p>"
                    + "<p>The E-commerce Support Team</p>"
                    + "</div>";
            helper.setText(htmlBody, true);

            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File logo = new File(Objects.requireNonNull(classLoader.getResource(logoPath)).getFile());
            helper.addInline("image_logo", logo);

            // Gửi email
            mailSender.send(message);

            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Send_Otp_Success)
                    .build();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseMessageDtos.builder()
                .status(500)
                .message(Constants.Send_Otp_Fail)
                .build();
    }

    public static String generateSku() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase().substring(0, 10);
    }
//----------------------------------Demo-------------------------------------------------------------
//    public void setMailSender(
//            String toEmail,
//            String subject,
//            String body){
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("ecommercedemo47@gmail.com");
//        message.setTo(toEmail);
//        message.setText(body);
//        message.setSubject(subject);
//
//        mailSender.send(message);
//        System.out.println("Mail Sent Successfully !!!");
//    }

//----------------------------------Test-------------------------------------------------------------
//    public void sendEmail(String toEmail, String subject, String body, String logoPath) {
//        MimeMessage message = mailSender.createMimeMessage();
//
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setFrom("ecommercedemo47@gmail.com");
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//
//            // Tạo nội dung HTML với link liên kết và logo
//            String htmlBody = "<div style='text-align: center;'>"
//                    + "<h1>" + body + "</h1>"
//                    + "<p>Click <a href='https://www.pinterest.com/pin/710020697515286358/'>here</a> to visit the website.</p>"
//                    + "<img src='cid:image_logo' style='display: block; margin: 0 auto;' />"
//                    + "</div>";
//
//            helper.setText(htmlBody, true);
//
//            // Đính kèm logo
//            File logo = new File(logoPath);
//            helper.addInline("image_logo", logo);
//
//            mailSender.send(message);
//            System.out.println("Mail Sent Successfully !!!");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
}
