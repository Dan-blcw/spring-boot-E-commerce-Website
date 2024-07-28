package com.dan_michael.example.demo.chatbot.service;

import java.util.List;
import java.util.stream.Collectors;

import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.resository.QuestionAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    public String handleInput(String input) {
        QuestionAnswer qa = questionAnswerRepository.findByQuestion(input.toLowerCase());
        if (qa != null) {
            return qa.getAnswer();
        } else {
            return "I'm sorry, I didn't understand that. Could you please rephrase?";
        }
    }
    
    public QuestionAnswer createQuestionAnswer(QuestionAnswer qa) {
        return questionAnswerRepository.save(qa);
    }
    
    public QuestionAnswer updateQuestionAnswer(String question, String newAnswer) {
        QuestionAnswer qa = questionAnswerRepository.findByQuestion(question.toLowerCase());
        if(qa != null){
            qa.setAnswer(newAnswer);
            questionAnswerRepository.save(qa);
        }
        return qa;
    }
    
    public void deleteQuestionAnswer(String question) {
        QuestionAnswer qa = questionAnswerRepository.findByQuestion(question.toLowerCase());
        if(qa != null){
            questionAnswerRepository.delete(qa);
        }
    }
    
    public List<String> getAllQuestions() {
    	return questionAnswerRepository.findAll().stream()
                .map(QuestionAnswer::getQuestion)
                .collect(Collectors.toList());    }
    }