package com.dan_michael.example.demo.chatbot.controller;

import java.util.List;

import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.RequestMessageChatBotDtos;
import com.dan_michael.example.demo.chatbot.service.ChatbotService;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/rest")
public class ChatRestController {

    private final ChatbotService chatBotService;

    @GetMapping("/getAllQuestions")
    public List<String> questions() {
        List<String> questions = chatBotService.getAllQuestions();
        return questions;
    }
    @PostMapping("/chat")
    public String chat(@RequestBody RequestMessageChatBotDtos message) {
    	return chatBotService.handleInput(message);
    }

    
    @PostMapping("/qa")
    public QuestionAnswer createQuestionAnswer(@RequestBody QuestionAnswer qa) {
        return chatBotService.createQuestionAnswer(qa);
    }
    
    @PutMapping("/qa")
    public QuestionAnswer updateQuestionAnswer(@RequestBody QuestionAnswer request) {
        return chatBotService.updateQuestionAnswer(request.getQuestion(),request.getAnswer());
    }

    
    @DeleteMapping("/qa")
    public ResponseMessageDtos deleteQuestionAnswer(@RequestBody RequestMessageChatBotDtos question) {
        var flag = chatBotService.deleteQuestionAnswer(question.getMessage());
        if(flag){
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message("Delete Question Answer AI Successfully !!!")
                    .build();
        }
        return ResponseMessageDtos.builder()
                .status(400)
                .message("Delete Question Answer AI Failure !!!")
                .build();
    }
    
}