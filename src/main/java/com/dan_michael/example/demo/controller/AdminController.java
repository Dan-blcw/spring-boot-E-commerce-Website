package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CategoryDtos;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.service.CategoryService;
import com.dan_michael.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService service;

    private final CategoryService categoryService;

//---------------------------Product---------------------------------------
    @PostMapping(value = "/add-product",consumes = { "multipart/form-data" })
    public ResponseEntity<?> createProduct(
            @ModelAttribute ProductDtos request
    ) {
        Product response = service.save(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This Product already exist").status(400).build());
        }
    }
//---------------------------Category---------------------------------------
    @PostMapping(value = "/add-category",consumes = { "multipart/form-data" })
    public ResponseEntity<?> createCategory(
            @ModelAttribute CategoryDtos request
    ) {
        Category response = categoryService.save(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This Category already exist").status(400).build());
        }
    }

}
