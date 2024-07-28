package com.dan_michael.example.demo.chatbot.service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.RequestMessageChatBotDtos;
import com.dan_michael.example.demo.chatbot.resository.QuestionAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final QuestionAnswerRepository questionAnswerRepository;

    public String handleInput(RequestMessageChatBotDtos message) {
        QuestionAnswer qa = questionAnswerRepository.findByQuestion(removeDiacritics(message.getMessage().toLowerCase()));
        System.out.println(qa);
        if (qa != null) {
            return qa.getAnswer();
        } else {
            return "I'm sorry, I didn't understand that. Could you please rephrase?";
        }
    }
    
    public QuestionAnswer createQuestionAnswer(QuestionAnswer qa) {
        qa.setQuestion(removeDiacritics(qa.getQuestion().toLowerCase()));
        return questionAnswerRepository.save(qa);
    }
    
    public QuestionAnswer updateQuestionAnswer(String question, String newAnswer) {
        QuestionAnswer qa = questionAnswerRepository.findByQuestion(removeDiacritics(question.toLowerCase()));
        if(qa != null){
            qa.setAnswer(newAnswer);
            questionAnswerRepository.save(qa);
        }
        return qa;
    }
    
    public Boolean deleteQuestionAnswer(String question) {
        QuestionAnswer qa = questionAnswerRepository.findByQuestion(removeDiacritics(question.toLowerCase()));
        if(qa != null){
            questionAnswerRepository.delete(qa);
            return true;
        }
        return false;
    }
    
    public List<String> getAllQuestions() {
    	return questionAnswerRepository.findAll().stream()
                .map(QuestionAnswer::getQuestion)
                .collect(Collectors.toList());
    }
    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

}