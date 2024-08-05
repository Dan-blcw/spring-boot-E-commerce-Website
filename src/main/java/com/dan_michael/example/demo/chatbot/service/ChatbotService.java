package com.dan_michael.example.demo.chatbot.service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.dan_michael.example.demo.chatbot.entities.ManyQuestionAnswerDtos;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.entities.QuestionForGuest;
import com.dan_michael.example.demo.chatbot.entities.dtos.QuestionOfGuestInfoDtos;
import com.dan_michael.example.demo.chatbot.entities.dtos.RequestMessageChatBotDtos;
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

    private final QuestionAnswerRepository questionAnswerRepository;

    private final QuestionOfGuestRepository questionOfGuestRepository;

    private final UserRepository userRepository;
    public String handleInput(String message) {
        System.out.println(message);
        List<QuestionAnswer> qa = questionAnswerRepository.findByKey(removeDiacritics(message.toLowerCase()));
//        System.out.println(qa.get(0).getAnswer());
        if(qa != null){
            for(var x :qa){
                if (x != null && x.getAnswer() != null) {
                    return x.getAnswer();
//                    + "câu trả lời của bạn trả về kết quả nhiều hơn 1, nếu bạn muốn hỏi kĩ hơn về vấn đề này, bạn có thể tìm kiếm từ khóa ở bên cạch");
                } else if(x.getAnswer() != null){
                    return x.getAnswer();
                }
            }
        }
        return Constants.Chat_Bot_No_Answer;
    }
    
    public QuestionAnswer createQuestionAnswer(QuestionAnswer qa) {
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
        question_of_guest.setAnswer(request.getAnswer());
        questionOfGuestRepository.save(question_of_guest);
        questionAnswerRepository.save(
                QuestionAnswer.builder()
                        .question(question_of_guest.getQuestion())
                        .answer(request.getAnswer())
                .build());
        return ResponseMessageDtos.builder()
                .status(200)
                .message(Constants.Create_QuestionAnswer_For_Guest_Success)
                .build();
    }

    public ResponseMessageDtos createQuestionForGuest(QuestionOfGuestInfoDtos request) {
        var question_of_guest = questionOfGuestRepository.findByQuestionForAnwser(request.getQuestion());
        if(question_of_guest != null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Create_QuestionAnswer_For_Guest_Fail)
                    .build();
        }
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
    	return questionAnswerRepository.findAll().stream()
                .map(QuestionAnswer::getQuestion)
                .collect(Collectors.toList());
    }

    public List<String> getAllQuestionsOfGuest() {
        return questionOfGuestRepository.findByQuestionUnAnswered().stream()
                .map(QuestionForGuest::getQuestion)
                .collect(Collectors.toList());
    }
    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

}