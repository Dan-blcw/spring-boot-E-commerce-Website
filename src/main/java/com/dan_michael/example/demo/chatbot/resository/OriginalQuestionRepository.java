package com.dan_michael.example.demo.chatbot.resository;

import com.dan_michael.example.demo.chatbot.entities.OriginalQuestion;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OriginalQuestionRepository extends JpaRepository<OriginalQuestion, Integer> {
    @Query("SELECT pi FROM OriginalQuestion pi WHERE pi.question LIKE  %:question%")
    List<OriginalQuestion> findByKey(@Param("question") String question);
    @Query("SELECT pi FROM OriginalQuestion pi WHERE pi.question = :question")
    OriginalQuestion findByQuestion(@Param("question") String question);
}

