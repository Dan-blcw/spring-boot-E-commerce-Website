package com.dan_michael.example.demo.chatbot.resository;

import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.QuestionForGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionOfGuestRepository extends JpaRepository<QuestionForGuest, Integer> {
    @Query("SELECT pi FROM QuestionForGuest pi WHERE pi.question = :question")
    QuestionForGuest findByQuestionForAnwser(@Param("question") String question);

    @Query("SELECT pi FROM QuestionForGuest pi WHERE pi.question = :question AND pi.answer = :answer ")
    QuestionForGuest findByQuestionForAnwser(
            @Param("question") String question,
            @Param("answer") String answer
    );
    @Query("SELECT pi FROM QuestionForGuest pi WHERE pi.answer = null ")
    List<QuestionForGuest> findQuestionOfGuest();
}

