package com.dan_michael.example.demo.chatbot.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManyQuestionAnswerDtos {
    private List<QuestionAnswer> data;
}
