package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.RegisterDtos;
import com.dan_michael.example.demo.model.dto.ob.*;
import com.dan_michael.example.demo.model.entities.*;
import com.dan_michael.example.demo.model.response.SubBrandsResponse;
import com.dan_michael.example.demo.model.response.SubCategoryResponse;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.service.AuthenticationService;
import com.dan_michael.example.demo.service.CategoryService;
import com.dan_michael.example.demo.service.OrderService;
import com.dan_michael.example.demo.service.Payment.PaymentMethodsService;
import com.dan_michael.example.demo.service.ProductService;
import com.dan_michael.example.demo.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

//    private final OrderService orderService;

    private final AuthenticationService authenticationService;

    private final CategoryService categoryService;

    private final PaymentMethodsService paymentMethodsService;

//--------------------------- order ---------------------------------------

//    @GetMapping("/orders")
//    public List<Order> getAllOrders() {
//        return orderService.getAllOrders();
//    }
//
//    @GetMapping("/orders/classify")
//    public List<Order> getAllOrdersByOrderStatus(
//            @RequestParam(required = false) String orderStatus
//    ) {
//        return orderService.getAllOrdersByOrderStatus(orderStatus);
//    }
//--------------------------- Account ---------------------------------------
    @PostMapping("/add-admin")
    public ResponseEntity<?> createAdmin(@RequestBody RegisterDtos registerDtos){
        return ResponseEntity.ok(authenticationService.createAdmin(registerDtos));
    }

    @PostMapping("/all-user")
    public ResponseEntity<?> allUser(){
        return ResponseEntity.ok(authenticationService.allUser());
    }

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
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ProductDtos request = mapper.readValue(productDtosJson, ProductDtos.class);
        if(images !=null){
            request.setImages(images);
            System.out.println("Images not null");
        }else {
            System.out.println("Images null");
        }
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
//    @PostMapping(value = "/update-product-image/{id}/{imageMain}", consumes = { "multipart/form-data" })
//    public ResponseEntity<?> updateImage(
//            @PathVariable Integer id,
//            @PathVariable String imageMain,
//            @RequestPart("images") List<MultipartFile> images
//    ) throws IOException
//    {
//        var response = service.updateProduct(id,imageMain,images);
//        if (response != null) {
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.badRequest().body(
//                    ResponseMessageDtos.builder()
//                            .message(Constants.Product_ARD_Exist)
//                            .status(400)
//                            .build());
//        }
//    }
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
//---------------------------------------tradeMask----------------------------------------------------------------
    @GetMapping("/tradeMask")
    public List<TradeMark> getAllTradeMarks() {
        return service.findAllTradeMark();
    }

    @GetMapping("/tradeMask/{id}")
    public TradeMark getTradeMarkById(@PathVariable String id) {
        TradeMark tradeMark = service.findByTradeMarkId(id);
//        return tradeMark.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        return tradeMark;
    }

    @PostMapping("/tradeMask")
    public TradeMark createTradeMark(@RequestBody TradeMaskDtos tradeMark) {
        return service.saveTrask(tradeMark);
    }

    @PutMapping("/update-tradeMask")
    public ResponseEntity<TradeMark> updateTradeMark(@RequestBody TradeMaskDtos tradeMarkDetails) {
        TradeMark tradeMark = service.findByTradeMarkId(tradeMarkDetails.getTradeMarkName());
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

//----------------------------------styles---------------------------------------------------------------------
    @GetMapping("/styles")
    public List<Style> getAllStyles() {
        return service.getAllStyles();
    }

    @GetMapping("/styles/{id}")
    public Style getStyleById(@PathVariable String id) {
        Style style = service.getStyleById(id);
        return style;
    }

    @PostMapping("/styles")
    public ResponseEntity<Style> createStyle(@RequestBody StyleDtos style) {
        Style createdStyle = service.createStyle(style);
        return new ResponseEntity<>(createdStyle, HttpStatus.CREATED);
    }
    @PutMapping("/update-styles")
    public ResponseEntity<Style> updateStyle(@RequestBody StyleDtos tradeMarkDetails) {
        Style style = service.getStyleById(tradeMarkDetails.getStyleName());
        style.setDescription(tradeMarkDetails.getDescription());
        style.setStatus(tradeMarkDetails.getStatus());
        style.setImage_url(tradeMarkDetails.getImage_url());
        System.out.println(tradeMarkDetails.getDescription());
        if (style != null) {
            return ResponseEntity.ok(service.updateStyle(style));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/styles/{id}")
    public ResponseEntity<Void> deleteStyle(@PathVariable Integer id) {
        service.deleteStyle(id);
        return ResponseEntity.noContent().build();
    }
//----------------------------------Material---------------------------------------------------------------------
    @GetMapping("/material")
    public List<Material> getAllMaterials() {
        return service.getAllMaterials();
    }

    @GetMapping("/material/{id}")
    public Material getMaterialById(@PathVariable String id) {
        Material material = service.getMaterialById(id);
        return material;
    }

    @PostMapping("/material")
    public ResponseEntity<Material> createMaterial(@RequestBody MaterialDtos material) {
        Material createdMaterial = service.createMaterial(material);
        return new ResponseEntity<>(createdMaterial, HttpStatus.CREATED);
    }

    @PutMapping("update-material")
    public ResponseEntity<Material> updateMaterial(@RequestBody MaterialDtos updatedMaterial) {
        Material material = service.getMaterialById(updatedMaterial.getMaterialName());
        material.setDescription(updatedMaterial.getDescription());
        material.setStatus(updatedMaterial.getStatus());
        material.setImage_url(updatedMaterial.getImage_url());
        System.out.println(updatedMaterial.getDescription());
        if (material != null) {
            return ResponseEntity.ok(service.updateMaterial(material));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/material/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Integer id) {
        service.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}

