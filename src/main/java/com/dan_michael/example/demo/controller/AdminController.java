package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.ob.*;
import com.dan_michael.example.demo.model.entities.TradeMark;
import com.dan_michael.example.demo.model.response.SubBrandsResponse;
import com.dan_michael.example.demo.model.response.SubCategoryResponse;
import com.dan_michael.example.demo.model.entities.PaymentMethods;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.service.CategoryService;
import com.dan_michael.example.demo.service.Payment.PaymentMethodsService;
import com.dan_michael.example.demo.service.ProductService;
import com.dan_michael.example.demo.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
//        Kiểm tra định dạng JsonDtos gửi từ front End có đúng không
//        System.out.println("Incoming ProductDtos: " + request);
//        request.getQuantityDetails().forEach(subColor -> {
//        System.out.println("Color: " + subColor.getColor());
//        subColor.getSizes().forEach(sizeQuantity -> {
//            System.out.println("Size: " + sizeQuantity.getSize());
//            System.out.println("Quantity: " + sizeQuantity.getQuantity());
//        });
//    });
//    System.out.println("Quantity: " + images.get(0).getOriginalFilename());
    @PostMapping(value = "/add-product", consumes = { "multipart/form-data" })
    public ResponseEntity<?> createProduct(
        @RequestPart("productDtos") String productDtosJson,
        @RequestPart("images") List<MultipartFile> images) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        ProductDtos request = mapper.readValue(productDtosJson, ProductDtos.class);
        request.setImages(images);
        var response = service.createProduct(request);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(
                    ResponseMessageDtos.builder()
                            .message(Constants.Product_ARD_Exist)
                            .status(400)
                            .build());
        }
}

    @PostMapping(value = "/add-product-json")
    public ResponseEntity<?> createProductTest(
            @RequestBody ProductDtos request
    ) {
        var response = service.createProduct(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(
                    ResponseMessageDtos.builder()
                            .message(Constants.Product_ARD_Exist)
                            .status(400).build()
            );
        }
    }
//socket, chatbot, devops, livestream, Mã QR,
    @PostMapping(value = "/update-product", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateProduct(
            @RequestPart("productDtos") String productDtosJson,
            @RequestPart("images") List<MultipartFile> images) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ProductDtos request = mapper.readValue(productDtosJson, ProductDtos.class);
        System.out.println("Quantity: " + images.get(0).getOriginalFilename());
        request.setImages(images);
        var response = service.updateProduct(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(
                    ResponseMessageDtos.builder()
                            .message(Constants.Product_Update_Fail)
                            .status(400)
                            .build()
            );
        }
    }

    @PutMapping(value = "/update-product-json")
    public ResponseEntity<?> updateProductTest(
            @RequestBody ProductDtos request
    ) {
        var response = service.updateProduct(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(
                    ResponseMessageDtos.builder()
                            .message(Constants.Product_Update_Fail)
                            .status(400)
                            .build()
            );
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

//---------------------------Category And Brand---------------------------------------
    @GetMapping(value = "/brands")
    public SubBrandsResponse findBrandsBy(
            @RequestParam Integer category_id
    ){
        var response = categoryService.findBrandByCategoryID(category_id);
        if(response == null){
            return SubBrandsResponse.builder()
                    .brands(response)
                    .message(Constants.Fetch_Data_Brand_Fail)
                    .build();
        }
        return SubBrandsResponse.builder()
                .brands(response)
                .message(Constants.Fetch_Data_Brand_Success)
                .build();
    }
    @GetMapping(value = "/categorys")
    public List<SubCategoryResponse> listCategory(){
        return categoryService.listCategory();
    }
    @GetMapping(value = "/detail-category/{id}")
    public SubCategoryResponse detailCategory(
            @PathVariable Integer id
    ){
        var detailCategory = categoryService.detailCategory(id);
        return detailCategory;
    }
    @PostMapping(value = "/add-category")
    public ResponseEntity<?> createCategory(
            @RequestBody CategoryDtos request
    ) {
        SubCategoryResponse response = categoryService.createCategory(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(
                    ResponseMessageDtos.builder()
                            .message(Constants.Category_ARD_Exist)
                            .status(400)
                            .build()
            );
        }
    }

    @PutMapping(value = "/update-category")
    public ResponseEntity<?> updateCategory(
            @RequestBody CategoryDtos request
    ) {
        SubCategoryResponse response = categoryService.updateCategory(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(
                    ResponseMessageDtos.builder()
                            .message(Constants.Category_ARD_Exist)
                            .status(400).build()
            );
        }
    }

    @DeleteMapping(value = "/delete-category/{id}")
    public ResponseMessageDtos DeleteCategory(
            @PathVariable Integer id
    ) {
        return categoryService.removeCategory(id);
    }

    @DeleteMapping(value = "/delete-category/{category_id}/")
    public ResponseMessageDtos DeleteBrand(
            @PathVariable Integer category_id,
            @RequestParam String brandName
    ) {
        return categoryService.removeBrand(category_id,brandName);
    }


    //---------------------------PaymentMethods---------------------------------------
    @GetMapping(value = "/payments-methods")
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
    @PostMapping(value = "/add-payment-methods")
    public ResponseEntity<?> createPaymentMethods(
            @RequestBody PaymentMethodsDtos request
    )  {
        var response = paymentMethodsService.createPaymentMethods(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(
                    ResponseMessageDtos.builder()
                            .message(Constants.Payment_Methods_ARD_Exist)
                            .status(400).build()
            );
        }
    }

    @PutMapping(value = "/update-payment-methods")
    public ResponseEntity<?> updatePaymentMethods(
            @RequestBody PaymentMethodsDtos request
    ) {
        var response = paymentMethodsService.updatePaymentMethods(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(
                    ResponseMessageDtos.builder()
                            .message(Constants.Payment_Methods_ARD_Exist)
                            .status(400).build()
            );
        }
    }

    @DeleteMapping(value = "/delete-payment-methods/{id}")
    public ResponseMessageDtos DeletePaymentMethods(
            @PathVariable Integer id
    ) {
        return paymentMethodsService.remove(id);
    }
//-------------------------------------------------------------------------------------------------------
    @GetMapping("/tradeMask")
    public List<TradeMark> getAllTradeMarks() {
        return service.findAll();
    }

    @GetMapping("/tradeMask/{id}")
    public TradeMark getTradeMarkById(@PathVariable String id) {
        TradeMark tradeMark = service.findById(id);
//        return tradeMark.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        return tradeMark;
    }

    @PostMapping("/tradeMask")
    public TradeMark createTradeMark(@RequestBody TradeMaskDtos tradeMark) {
        return service.saveTrask(tradeMark);
    }

    @PutMapping("/update-tradeMask")
    public ResponseEntity<TradeMark> updateTradeMark(@RequestBody TradeMaskDtos tradeMarkDetails) {
        TradeMark tradeMark = service.findById(tradeMarkDetails.getTradeMarkName());
        tradeMark.setDescription(tradeMarkDetails.getDescription());
        tradeMark.setStatus(tradeMarkDetails.getStatus());
        tradeMark.setImage_url(tradeMarkDetails.getImage_url());
        System.out.println(tradeMarkDetails.getDescription());
        if (tradeMark != null) {
            return ResponseEntity.ok(service.updateTrask(tradeMark));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/tradeMask/{id}")
    public ResponseEntity<Void> deleteTradeMark(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

