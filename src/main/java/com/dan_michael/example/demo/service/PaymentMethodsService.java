package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.CategoryDtos;
import com.dan_michael.example.demo.model.dto.ob.PaymentMethodsDtos;
import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.PaymentMethods;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.repositories.CategoryRepository;
import com.dan_michael.example.demo.repositories.PaymentMethodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentMethodsService {

    private final PaymentMethodsRepository repository;

    public PaymentMethods createPaymentMethods(PaymentMethodsDtos request) {

        var ob = repository.findPaymentMethodsByName_(request.getPaymentMethodsName());

        if (ob != null) {
            return null;
        }
        PaymentMethods category_flag = PaymentMethods.builder()
                .name(request.getPaymentMethodsName())
                .description(request.getDescription())
                .createdDate(new Date())
                .status(request.getStatus())
                .build();
        repository.save(category_flag);
        return category_flag;
    }

    public PaymentMethods updatePaymentMethods(PaymentMethodsDtos request) {

        var category_flag = repository.findPaymentMethodsByName_(request.getPaymentMethodsName());

        if(category_flag != null){
            category_flag.setName(request.getPaymentMethodsName());
            category_flag.setDescription(request.getDescription());
            category_flag.setStatus(request.getStatus());
            repository.save(category_flag);
        }
        return category_flag;
    }

    public List<PaymentMethods> listPaymentMethods() {
        return repository.findAll();
    }

    public Optional<PaymentMethods> detailPaymentMethods(Integer id) {
        return repository.findById(id);
    }

    public ResponseMessageDtos remove(Integer id) {
        var flag = repository.findById(id);
        if(flag.isPresent()){
            repository.deleteById(id);
            return ResponseMessageDtos.builder().status(200).message("Delete Payment Methods successfully !!").build();
        }else {
            return ResponseMessageDtos.builder().status(400).message("Delete Payment Methods fail !!").build();
        }
    }
}
