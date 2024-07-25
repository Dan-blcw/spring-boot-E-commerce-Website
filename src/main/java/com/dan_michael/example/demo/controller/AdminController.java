package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.ob.PaymentMethodsDtos;
import com.dan_michael.example.demo.model.entities.PaymentMethods;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CategoryDtos;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.service.CategoryService;
import com.dan_michael.example.demo.service.PaymentMethodsService;
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

    private final PaymentMethodsService paymentMethodsService;

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
//socket, chatbot, devops, livestream, Mã QR,
    @PutMapping(value = "/update-product",consumes = { "multipart/form-data" })
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

    @DeleteMapping(value = "/remove-product/{id}")
    public ResponseEntity<?> removeProduct(
            @PathVariable Integer id
    ) {
        ResponseMessageDtos response = service.removebyId(id);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(response);
        }
    }

//---------------------------Category---------------------------------------
    @GetMapping(value = "/list-category")
    public List<Category> listCategory(@ModelAttribute CategoryDtos request){
        return categoryService.listCategory();
    }
    @GetMapping(value = "/detail-category/{id}")
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

    @PutMapping(value = "/update-category",consumes = { "multipart/form-data" })
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

    @DeleteMapping(value = "/delete-category/{id}")
    public ResponseMessageDtos DeleteCategory(
            @PathVariable Integer id
    ) {
        return categoryService.remove(id);
    }

//---------------------------PaymentMethods---------------------------------------
    @GetMapping(value = "/list-payment-methods")
    public List<PaymentMethods> listPaymentMethods(){
        return paymentMethodsService.listPaymentMethods();
    }
    @GetMapping(value = "/detail-payment-methods/{id}")
    public Optional<PaymentMethods> detailPaymentMethods(
            @PathVariable Integer id
    ){
        var paymentMethods = paymentMethodsService.detailPaymentMethods(id);
        return paymentMethods;
    }
    @PostMapping(value = "/add-payment-methods",consumes = { "multipart/form-data" })
    public ResponseEntity<?> createPaymentMethods(
            @ModelAttribute PaymentMethodsDtos request
    ) {
        var response = paymentMethodsService.createPaymentMethods(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This PaymentMethods already exist").status(400).build());
        }
    }

    @PutMapping(value = "/update-payment-methods",consumes = { "multipart/form-data" })
    public ResponseEntity<?> updatePaymentMethods(
            @ModelAttribute PaymentMethodsDtos request
    ) {
        var response = paymentMethodsService.updatePaymentMethods(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This PaymentMethods already exist").status(400).build());
        }
    }

    @PostMapping(value = "/delete-payment-methods/{id}")
    public ResponseMessageDtos DeletePaymentMethods(
            @PathVariable Integer id
    ) {
        return paymentMethodsService.remove(id);
    }
}
