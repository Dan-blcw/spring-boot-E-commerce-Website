package com.dan_michael.example.demo.controller;


import com.dan_michael.example.demo.chat_socket.entities.ChatImg;
import com.dan_michael.example.demo.chat_socket.respository.ChatImgRepository;
import com.dan_michael.example.demo.chat_socket.service.ChatMessageService;
import com.dan_michael.example.demo.chatbot.entities.dtos.QuestionOfGuestInfoDtos;
import com.dan_michael.example.demo.chatbot.entities.dtos.RequestMessageChatBotDtos;
import com.dan_michael.example.demo.chatbot.service.ChatbotService;
import com.dan_michael.example.demo.model.dto.global.ChangeForgetPasswordDtos;
import com.dan_michael.example.demo.model.dto.global.ForgetPasswordDtos;
import com.dan_michael.example.demo.model.dto.global.PaginationDto;
import com.dan_michael.example.demo.model.dto.ob.ProductListDtos;
import com.dan_michael.example.demo.model.entities.Discount;
import com.dan_michael.example.demo.model.response.ProductResponse;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.repositories.DiscountRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import com.dan_michael.example.demo.repositories.image.ProductImgRepository;
import com.dan_michael.example.demo.repositories.image.UserImgRepository;
import com.dan_michael.example.demo.service.ProductService;
import com.dan_michael.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/global")
@RequiredArgsConstructor
public class GuestController {

    private final ProductService service;

    private final UserService userService;

    private final ChatbotService chatBotService;

    private final ChatImgRepository chatImgRepository;

    private final ProductImgRepository productImgRepository;

    private final UserImgRepository userImgRepository;

    private final DiscountRepository discountRepository;

//--------------------------Except discount----------------------------------
    @PostMapping("/check-discount-sku")
    public Discount CheckDiscount(@RequestParam String sku){
        return discountRepository.findBySku(sku);
    }

//--------------------------Hỏi Thằng Hương ấy----------------------------------
//    @DeleteMapping("/delete-message/{id}")
//    public ResponseMessageDtos DeleteMessage(@PathVariable Integer id) {
//        return chatMessageService.delete(id);
//    }
//
//    @PutMapping("/update-message/{id}")
//    public ResponseMessageDtos UpdateMessage(@PathVariable Integer id,@RequestBody String reContent) {
//        return chatMessageService.update(id,reContent);
//    }
//--------------------------Quest of Guess----------------------------------
    @GetMapping("/")
    public String getResponse(@RequestParam String question) {
        return chatBotService.handleInput(question);
    }
    @PostMapping("/qa-create-question-guest")
    public ResponseMessageDtos createQuestionForGuest(@RequestBody QuestionOfGuestInfoDtos request) {
        return chatBotService.createQuestionForGuest(request);
    }
//--------------------------Forget Password----------------------------------
    @GetMapping(value = "/check-forget-password")
    public ResponseMessageDtos checkForgetPassword(
            @RequestBody ForgetPasswordDtos request
    ) {
        return userService.checkAccountForgetPassword(request);
    }
    @PatchMapping(value = "/update-forget-password")
    public ResponseEntity<?> forgetPassword(
            @RequestBody ChangeForgetPasswordDtos request
    ) {
        return ResponseEntity.ok(userService.changeAccountForgetPassword(request));
    }

//--------------------------Read Only Product----------------------------------
    @GetMapping(value = "/detail-product")
    public ResponseEntity<?> detail(
            @RequestParam (required = false)Integer id

    ) {
        var ob = service.findbyIDHander(id);
        return ResponseEntity.ok(ob);
    }
    @GetMapping(value = "/products")
    public ResponseEntity<?> get_All(
            @RequestParam (required = false)Integer _limit,
        @RequestParam (required = false)Integer _total
    ) {
        var list = service.findAllHander();
        return ResponseEntity.ok(
                ProductListDtos.builder()
                        .data(list)
                        .paginationDto(new PaginationDto(_total,_limit))
                        .build());
    }

    @GetMapping(value = "/products-chat-bot")
    public ResponseEntity<?> get_All_Pros_Chat_Bot() {
        var saves = service.findProduct_ChatBot();
        return ResponseEntity.ok(saves);
    }

    @GetMapping(value = "/products-chat-bot-image")
    public ResponseEntity<?> get_All_ticker_Chat_Bot() {
        var saves = chatImgRepository.findAll();
        return ResponseEntity.ok(saves);
    }
    @PostMapping(value = "/products-chat-bot-image-post")
    public ResponseEntity<?> post_All_ticker_Chat_Bot(List<String> images) {
        for(var x: images){
            if(chatImgRepository.findChatImgByUrl(x) == null){
                chatImgRepository.save(ChatImg.builder()
                        .img_url(x)
                        .build());
            }

        }
        return ResponseEntity.ok(images);
    }

//--------------------------favorite----------------------------------
    @GetMapping(value = "/favorite-products")
    public ResponseEntity<?> getFavoriteByUser_id(
            @RequestParam (required = false)Integer use_id

    ) {
        var ob = service.findbyFavouriteByUserID(use_id);
        return ResponseEntity.ok(ob);
    }
    @PostMapping(value = "/add-favorite")
    public ResponseEntity<?> addFavorite(
            @RequestParam (required = false)String Product_name,
            @RequestParam (required = false)Integer use_id
    ) {
        var response = service.addFavourite(Product_name,use_id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete-favorite")
    public ResponseEntity<?> deleteFavorite(
            @RequestParam (required = false)String Product_name,
            @RequestParam (required = false)Integer use_id
    ) {
        var response = service.deleteFavourite(Product_name,use_id);
        return ResponseEntity.ok(response);
    }
//--------------------------QuantityDetail----------------------------------

    @GetMapping(value = "/delete-favorite")
    public ResponseEntity<?> getDetail(
            @RequestParam (required = false)String Product_name,
            @RequestParam (required = false)Integer use_id
    ) {
        var response = service.deleteFavourite(Product_name,use_id);
        return ResponseEntity.ok(response);
    }

//--------------------------Search----------------------------------
    @GetMapping(value = "/list-search",produces = "application/json")
    public ResponseEntity<?> global_search(
            @RequestParam (required = false) String categoryName,
            @RequestParam (required = false)List<String> subCategoryName,
            @RequestParam (required = false)Boolean isPromotion,
            @RequestParam (required = false)Boolean isReleased,
            @RequestParam (required = false)Integer ratingGte,
            @RequestParam (required = false)Integer price_gte,
            @RequestParam (required = false)Integer price_lte,
            @RequestParam (required = false)Boolean isBestSelling,
            @RequestParam Integer _limit,
            @RequestParam Integer _page,
            @RequestParam (required = false)String _sort
    ) {
        List<ProductResponse> list = service.search_all(categoryName,subCategoryName,isPromotion,isReleased,ratingGte,price_gte,price_lte,_sort,isBestSelling);
        return ResponseEntity.ok(ProductListDtos.builder().data(list).paginationDto(new PaginationDto(list.size(),_limit)).build());
    }

//--------------------------GetQuantityDetail----------------------------------
    @GetMapping(value = "/detail-product/{product_name}/get-color-detail")
    public ResponseEntity<?> getColorDetail(
            @PathVariable String product_name,
            @RequestParam (required = false)String color
    ) {
        var response = service.getSizeQuantityByColorAndproductname(product_name,color);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/detail-product/{product_name}/get-quantitys-total")
    public ResponseEntity<?> getDetailQuantity(
            @PathVariable String product_name
    ) {
        var response = service.getQuantityTotal(product_name);
        return ResponseEntity.ok(response);
    }
//--------------------------load File File name----------------------------------

    @GetMapping("/media/images/{name}/get-user-img")
    public String getUserImg_Url(@PathVariable String name) {
        var image_user = userImgRepository.findUserImgByUserName(name);
        if (image_user != null) {
            return image_user.getImg_url();
        }
        return "";
    }
    @GetMapping("/media/images/{name}/{filename}")
    public ResponseEntity<ByteArrayResource> getImageByFilename(
            @PathVariable String name,
            @PathVariable String filename
    ) throws IOException {
        var image_product = productImgRepository.findProductImgByimageName(filename,name);
        if (image_product.isPresent()) {
            byte[] imageBytes = image_product.get().getImage();
            ByteArrayResource resource = new ByteArrayResource(imageBytes);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        }
        if(!image_product.isPresent()){
            var image_user = userImgRepository.findUserImgByimageName(filename,name);
            if (image_user.isPresent()) {
                byte[] imageBytes = image_user.get().getImage();
                ByteArrayResource resource = new ByteArrayResource(imageBytes);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }
        }
        return ResponseEntity.notFound().build();
    }

//    @GetMapping("/payos")
//    public String getImageByFilename(
//    ) throws Exception {
//        String clientId = "your-client-id";
//        String apiKey = "your-api-key";
//        String checksumKey = "your-checksum-key";
//        String partnerCode = "your-partner-code";
//        String webhookUrl = "https://www.instagram.com/";
//        String webhookUrl_cancel = "https://www.facebook.com/";
//        PayOS payOS = new PayOS(clientId, apiKey, checksumKey);
//
//        // or with partnerCode
//        ItemData itemData = ItemData.builder().name("Mỳ tôm Hảo Hảo ly").quantity(1).price(2000).build();
//
//        PaymentData paymentData = PaymentData.builder()
//                .orderCode(653165l).amount(2000)
//                .description("Thanh toán đơn hàng")
////                .returnUrl(webhookUrl + "/success")
////                .cancelUrl(webhookUrl + "/cancel")
//                .returnUrl(webhookUrl)
//                .cancelUrl(webhookUrl_cancel)
//                .item(itemData).build();
//
//        CheckoutResponseData data = payOS.createPaymentLink(paymentData);
//        return data.getCheckoutUrl();
////        CheckoutResponseData data = payOS.createPaymentLink(paymentData);
////        PayOS payOS = new PayOS(clientId, apiKey, checksumKey, partnerCode);
////        return payOS.getPaymentLinkInformation();
//
//    }
}
