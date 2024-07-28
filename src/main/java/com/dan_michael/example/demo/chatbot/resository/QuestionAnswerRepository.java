package com.dan_michael.example.demo.chatbot.resository;

import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Integer> {
    @Query("SELECT pi FROM QuestionAnswer pi WHERE pi.question = :question")
    QuestionAnswer findByQuestion(@Param("question") String question);
}

