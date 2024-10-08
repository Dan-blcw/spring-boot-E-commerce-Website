package com.dan_michael.example.demo.chatbot.controller;

import java.util.List;

import com.dan_michael.example.demo.chatbot.entities.ManyQuestionAnswerDtos;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.QuestionForGuest;
import com.dan_michael.example.demo.chatbot.entities.dtos.RequestMessageChatBotDtos;
import com.dan_michael.example.demo.chatbot.resository.QuestionOfGuestRepository;
import com.dan_michael.example.demo.chatbot.service.ChatbotService;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.service.EmailSenderService;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rest")
public class ChatRestController {

    private final EmailSenderService emailSenderService;

    private final ChatbotService chatBotService;

    private final QuestionOfGuestRepository questionOfGuestRepository;
    @GetMapping("/getAllQuestions")
    public List<String> questions() {
        List<String> questions = chatBotService.getAllQuestions();
        return questions;
    }

    @GetMapping("/all")
    public ResponseEntity<?> All() {
        return ResponseEntity.ok(chatBotService.all());
    }

    @PostMapping("/chat")
    public String chat(@RequestBody RequestMessageChatBotDtos message) {
    	return chatBotService.handleInput(message.getMessage());
    }
    @PostMapping("/qa")
    public QuestionAnswer createQuestionAnswer(@RequestBody QuestionAnswer qa) {
        return chatBotService.createQuestionAnswer(qa);
    }
    @PostMapping("/many-qa")
    public List<QuestionAnswer> createManyQuestionAnswer(@RequestBody ManyQuestionAnswerDtos qa) {
        return chatBotService.createManyQuestionAnswer(qa);
    }

    
    @PutMapping("/qa/{id}")
    public QuestionAnswer updateQuestionAnswer(
            @RequestBody QuestionAnswer request,
            @PathVariable Integer id
    ) {
        return chatBotService.updateQuestionAnswer(request.getAnswer(),id);
    }

    
    @DeleteMapping("/qa/{id}")
    public ResponseMessageDtos deleteQuestionAnswer(@PathVariable Integer id) {
        var response = chatBotService.deleteQuestionAnswer(id);
        if(response){
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Delete_Answer_Question_AI_Success)
                    .build();
        }
        return ResponseMessageDtos.builder()
                .status(400)
                .message(Constants.Delete_Answer_Question_AI_Fail)
                .build();
    }
//---------------------------------Guest-------------------------------------------
    @GetMapping("/getAllQuestionsOfGuest")
    public List<String> questionsOfGuest() {
        List<String> questions = chatBotService.getAllQuestionsOfGuest();
        return questions;
    }

    @GetMapping("/getAllQuestionsOfGuestInfor")
    public List<QuestionForGuest> questionsOfGuestInfo() {
        List<QuestionForGuest> questions = chatBotService.getAllQuestionsOfGuestInfo();
        return questions;
    }

    @PostMapping("/qa-answer-guest")
    public ResponseMessageDtos createQuestionAnswerOfGuest(
            @RequestBody QuestionAnswer qa
    ) {
        var save = chatBotService.createQuestionAnswerForGuest(qa);
        var question_ = questionOfGuestRepository.findByQuestionForAnwser(qa.getQuestion(), qa.getAnswer());
        if(question_ != null){
            emailSenderService.sendEmailAnswer(
                    question_.getEmail(),
                    Constants.Subject_Answer_Question,
                    question_.getName(),
                    question_.getQuestion(),
                    question_.getAnswer(),
                    Constants.Logo_Path_0
            );
        }
        return save;
    }
    
}