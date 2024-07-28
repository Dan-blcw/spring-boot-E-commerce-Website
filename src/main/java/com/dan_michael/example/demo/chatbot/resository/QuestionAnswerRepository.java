package com.dan_michael.example.demo.chatbot.resository;

import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Integer> {

    QuestionAnswer findByQuestion(String question);
}

