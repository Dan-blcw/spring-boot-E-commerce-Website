package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CategoryDtos;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.service.CategoryService;
import com.dan_michael.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService service;

    private final CategoryService categoryService;

//--------------------------- Product(CUD - R in GuestController) ---------------------------------------

    @PostMapping(value = "/add-product",consumes = { "multipart/form-data" })
    public ResponseEntity<?> createProduct(
            @ModelAttribute ProductDtos request
    ) {
        Product response = service.createProduct(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This Product already exist").status(400).build());
        }
    }

    @PostMapping(value = "/update-product",consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateProduct(
            @ModelAttribute ProductDtos request
    ) {
        Product response = service.updateProduct(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("ERROR! From Update Product").status(400).build());
        }
    }

    @DeleteMapping(value = "/remove-product/{product_id}",consumes = { "multipart/form-data" })
    public ResponseEntity<?> removeProduct(
            @PathVariable Integer product_id
    ) {
        ResponseMessageDtos response = service.removebyId(product_id);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(response);
        }
    }
//---------------------------Category---------------------------------------

    @GetMapping(value = "/list-category",consumes = { "multipart/form-data" })
    public List<Category> listCategory(@ModelAttribute CategoryDtos request){
        return categoryService.listCategory();
    }
    @GetMapping(value = "/detail-category/{category_id}",consumes = { "multipart/form-data" })
    public Optional<Category> detailCategory(
            @PathVariable Integer id
    ){
        var detailCategory = categoryService.detailCategory(id);
        return detailCategory;
    }
    @PostMapping(value = "/add-category",consumes = { "multipart/form-data" })
    public ResponseEntity<?> createCategory(
            @ModelAttribute CategoryDtos request
    ) {
        Category response = categoryService.createCategory(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This Category already exist").status(400).build());
        }
    }

    @PostMapping(value = "/update-category",consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateCategory(
            @ModelAttribute CategoryDtos request
    ) {
        Category response = categoryService.updateCategory(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This Category already exist").status(400).build());
        }
    }

    @PostMapping(value = "/deleta-category/{category_id}")
    public ResponseMessageDtos DeleteCategory(
            @PathVariable Integer id
    ) {
        return categoryService.remove(id);
    }
}
