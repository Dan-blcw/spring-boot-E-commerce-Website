package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.CategoryDtos;
import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public Category save(CategoryDtos request) {

        var ob = repository.findCategoryByName(request.getCategoryName());

        if(ob.isPresent()){
            return null;
        }

        var category_flag = Category.builder()
                .name(request.getCategoryName())
                .image(request.getImage())
                .description(request.getDescription())
                .createdDate(new Date())
                .status(1)
                .build();

        repository.save(category_flag);
        return category_flag;
    }


}
