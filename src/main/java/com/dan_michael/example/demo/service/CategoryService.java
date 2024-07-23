package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.global.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CategoryDtos;
import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public Category createCategory(CategoryDtos request) {

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

    public Category updateCategory(CategoryDtos request) {

        var category_flag = repository.findCategoryByName_(request.getCategoryName());

        if(category_flag != null){
            category_flag.setName(request.getCategoryName());
            category_flag.setDescription(request.getCategoryName());
            category_flag.setImage(request.getImage());
            category_flag.setStatus(request.getStatus());
        }
        repository.save(category_flag);
        return category_flag;
    }

    public List<Category> listCategory() {
        return repository.findAll();
    }

    public Optional<Category> detailCategory(Integer id) {
        return repository.findById(id);
    }

    public ResponseMessageDtos remove(Integer id) {
        var flag = repository.findById(id);
        if(flag.isPresent()){
            repository.deleteById(id);
            return ResponseMessageDtos.builder().status(200).message("Delete Category successfully !!").build();
        }else {
            return ResponseMessageDtos.builder().status(400).message("Delete Category fail !!").build();
        }
    }
}
