package com.dan_michael.example.demo.chatbot.entities.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOfGuestInfoDtos {
    private String question;
    private String name;
    private String email;
    private String phone;

}
