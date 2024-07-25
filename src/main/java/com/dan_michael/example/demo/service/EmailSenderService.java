package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.entities.User;
import com.dan_michael.example.demo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;
@Service
@RequiredArgsConstructor
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    private final UserRepository userRepository;
//---------------------------------Done-------------------------------------------------------------------
    public String sendEmail(String toEmail, String subject, String name,String body, String logoPath,String discountCode) {
        MimeMessage message = mailSender.createMimeMessage();
        var user = userRepository.findByEmail_(toEmail);
        if(user == null){
            return "User does not exist";
        }
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("ecommercedemo47@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            String htmlBody = "<div style='text-align: center; font-family: Arial, sans-serif; font-size: 15px;'>"
                    + "<img src='cid:image_logo' style='display: block; margin: 0 auto; max-width: 400px;' />"
                    + "<h1>Hi "+ name+ "!</h1>"
                    + "<p>"+ body + "</p>"
                    + "<div>"
                    + "<a href='http://www.facebook.com'><img src='cid:image_fb' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<a href='http://www.linkedin.com'><img src='cid:image_li' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<a href='http://www.youtube.com'><img src='cid:image_yt' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<a href='http://www.pinterest.com'><img src='cid:image_pin' style='width: 28px; height: 28px; margin: 0 5px;' /></a>"
                    + "<p>The E-commerce Team (Dan - Michael)</p>"
                    + "</div>"
                    + "</div>";

            helper.setText(htmlBody, true);

            File logo = new File(logoPath);
            helper.addInline("image_logo", logo);

            File fbIcon = new File("D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img_1.png");
            File liIcon = new File("D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img_2.png");
            File ytIcon = new File("D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img_3.png");
            File pinIcon = new File("D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img_4.png");

            helper.addInline("image_fb", fbIcon);
            helper.addInline("image_li", liIcon);
            helper.addInline("image_yt", ytIcon);
            helper.addInline("image_pin", pinIcon);

            mailSender.send(message);
            System.out.println("Mail Sent Successfully !!!");
            return "Mail Sent Successfully !!!";
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "Mail Sent Failure !!!";
}
    public String getDiscountCode(String toEmail, String subject, String name, String logoPath,String discountCode) {
        MimeMessage message = mailSender.createMimeMessage();
        var user = userRepository.findByEmail_(toEmail);
        if(user == null){
            return "User does not exist";
        }
        if(user.getUseFirstDiscount() == 1){
            return "This account has already used the first discount promotion!!!";
        }
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("ecommercedemo47@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            String htmlBody = "<div style='text-align: center; font-family: Arial, sans-serif; font-size: 15px;'>"
                    + "<img src='cid:image_logo' style='display: block; margin: 0 auto; max-width: 400px;' />"
                    + "<h1>Hi "+ name+ "!</h1>"
                    + "<p>Thanks for signing up with our E-commerce platform! You're about to join the largest online community of enthusiastic shoppers.</p>"
                    + "<p>As a welcome gift, enjoy a discount on your first order!</p>"
                    + "<p>Grab the Discount Code and return to our site and enter the discount code by clicking the link below</p>"
                    + "<p><a href='https://www.pinterest.com/pin/710020697515286358/'style='display: inline-block; margin: 20px auto; font-size: 32px; padding: 10px 20px; color: #000; background-color: #DDD6B9; text-decoration: none; border-radius: 5px;'> "+discountCode+ "</a></p>"
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

            File logo = new File(logoPath);
            helper.addInline("image_logo", logo);

            File fbIcon = new File("D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img_1.png");
            File liIcon = new File("D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img_2.png");
            File ytIcon = new File("D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img_3.png");
            File pinIcon = new File("D:\\Downloads\\PJ_CDTN\\demo\\src\\main\\resources\\img_4.png");

            helper.addInline("image_fb", fbIcon);
            helper.addInline("image_li", liIcon);
            helper.addInline("image_yt", ytIcon);
            helper.addInline("image_pin", pinIcon);

            mailSender.send(message);
            System.out.println("Mail Sent Successfully !!!");
            user.setUseFirstDiscount(1);
            userRepository.save(user);
            return "Mail Sent Successfully !!!";
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "Mail Sent Failure !!!";
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
