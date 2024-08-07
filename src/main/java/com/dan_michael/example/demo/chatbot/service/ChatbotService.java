package com.dan_michael.example.demo.chatbot.service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.dan_michael.example.demo.chatbot.entities.ManyQuestionAnswerDtos;
import com.dan_michael.example.demo.chatbot.entities.OriginalQuestion;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.QuestionForGuest;
import com.dan_michael.example.demo.chatbot.entities.dtos.QuestionOfGuestInfoDtos;
import com.dan_michael.example.demo.chatbot.entities.dtos.RequestMessageChatBotDtos;
import com.dan_michael.example.demo.chatbot.resository.OriginalQuestionRepository;
import com.dan_michael.example.demo.chatbot.resository.QuestionAnswerRepository;
import com.dan_michael.example.demo.chatbot.resository.QuestionOfGuestRepository;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.repositories.UserRepository;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final OriginalQuestionRepository originalQuestionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;

    private final QuestionOfGuestRepository questionOfGuestRepository;

    private final UserRepository userRepository;
    public String handleInput(String message) {
        List<QuestionAnswer> qa = questionAnswerRepository.findByKey(removeDiacritics(message.toLowerCase()));
        if(qa != null){
            for(var x = 0; x< qa.size();x ++){
                if(qa.get(0).getAnswer() !=null){
                    return qa.get(0).getAnswer();
                }else if(qa.get(x).getAnswer() !=null){
                    return (qa.get(x).getAnswer() + Constants.Chat_Bot_Many_Answer+ "("+qa.size()+" - Result) " + Constants.Chat_Bot_Many_Answer_2);
                }
            }
        }
        if(message.contains(Constants.Sticker_Path)){
            return Constants.Chat_Bot_No_Answer_Sticker;
        }
        return Constants.Chat_Bot_No_Answer;
    }
    
    public QuestionAnswer createQuestionAnswer(QuestionAnswer qa) {
        originalQuestionRepository.save(OriginalQuestion.builder()
                .question(qa.getQuestion())
                .build());
        qa.setQuestion(removeDiacritics(qa.getQuestion().toLowerCase()));
        return questionAnswerRepository.save(qa);
    }
    public ResponseMessageDtos createQuestionAnswerForGuest(QuestionAnswer request) {
        var question_of_guest = questionOfGuestRepository.findByQuestionForAnwser(request.getQuestion());
        if(question_of_guest == null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Question_Does_Not_Exists)
                    .build();
        }
        if(question_of_guest.getAnswer() != null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Answer_Exists)
                    .build();
        }
        var user = userRepository.findByEmail_(question_of_guest.getEmail());
        if(user==null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Create_QuestionAnswer_For_Guest_Fail)
                    .build();
        }
        question_of_guest.setUser_img_url(user.getUserImgUrl());
        question_of_guest.setAnswer(request.getAnswer());
        questionOfGuestRepository.save(question_of_guest);
        questionAnswerRepository.save(
                QuestionAnswer.builder()
                        .question(removeDiacritics(question_of_guest.getQuestion().toLowerCase()))
                        .answer(request.getAnswer())
                .build());
        return ResponseMessageDtos.builder()
                .status(200)
                .message(Constants.Create_QuestionAnswer_For_Guest_Success)
                .build();
    }

    public ResponseMessageDtos createQuestionForGuest(QuestionOfGuestInfoDtos request) {
        var question_of_guest = questionOfGuestRepository.findByQuestionForAnwser(removeDiacritics(request.getQuestion().toLowerCase()));
        if(question_of_guest != null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Question_Exists)
                    .build();
        }
        originalQuestionRepository.save(OriginalQuestion.builder()
                .question(request.getQuestion())
                .build());
        var user = userRepository.findByEmail_(request.getEmail());
        if(user==null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Create_Question_For_Guest_Fail)
                    .build();
        }
        var qa = QuestionForGuest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .user_img_url(user.getUserImgUrl())
                .phone(request.getPhone())
                .question(request.getQuestion())
                .build();
        questionOfGuestRepository.save(qa);
        return ResponseMessageDtos.builder()
                .status(200)
                .message(Constants.Create_Question_For_Guest_Success)
                .build();
    }
    public List<QuestionAnswer> createManyQuestionAnswer(ManyQuestionAnswerDtos qa) {
        for(var x : qa.getData()){
            x.setQuestion(removeDiacritics(x.getQuestion().toLowerCase()));
            questionAnswerRepository.save(x);
        }
        return qa.getData();
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
    	return originalQuestionRepository.findAll().stream()
                .map(OriginalQuestion::getQuestion)
                .collect(Collectors.toList());
    }

    public List<String> getAllQuestionsOfGuest() {
        return questionOfGuestRepository.findByQuestionUnAnswered().stream()
                .map(QuestionForGuest::getQuestion)
                .collect(Collectors.toList());
    }

    public List<QuestionForGuest> getAllQuestionsOfGuestInfo() {
        return questionOfGuestRepository.findByQuestionUnAnsweredNUl();
    }
    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

}