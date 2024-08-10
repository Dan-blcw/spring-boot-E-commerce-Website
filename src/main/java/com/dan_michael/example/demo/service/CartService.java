package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.CartDetailDtos;
import com.dan_michael.example.demo.model.dto.ob.sub.CartDtos;
import com.dan_michael.example.demo.model.entities.*;
import com.dan_michael.example.demo.model.entities.SubEn.CartDetail;
import com.dan_michael.example.demo.model.response.CartResponse;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.response.SubCart_OrderResponse;
import com.dan_michael.example.demo.repositories.SupRe.CartDetailRepository;
import com.dan_michael.example.demo.repositories.CartRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import com.dan_michael.example.demo.util.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartDetailRepository cartDetailRepository;

//    private final ProductRepository productRepository;

    private final UserRepository userRepository;

//---------------------------Cart & cartDetail---------------------------------------
    public List<CartResponse> getAllCarts() {
        List<CartResponse> save = new ArrayList<>();
        var flag = cartRepository.findAll();
        for (var x: flag){
            var tolalPay = 0.0d;
            var totalQua = 0;
            List<SubCart_OrderResponse> boxItem = new ArrayList<>();
            var detail = cartDetailRepository.findByIdentification_cart(x.getId());
            for(var y : detail){
                var itemCart = SubCart_OrderResponse.builder()
                        .itemDetail_id(y.getId())
                        .name(y.getName())
                        .size(y.getSize())
                        .image(y.getImage())
                        .color(y.getColor())
                        .quantity(y.getQuantity())
                        .unitPrice(y.getUnitPrice())
                        .totalPrice(y.getTotalPrice())
                        .build();
                boxItem.add(itemCart);
                tolalPay += y.getTotalPrice();
                totalQua += y.getQuantity();
            }
            save.add(
                    CartResponse.builder()
                            .cart_id(x.getId())
                            .user_id(x.getIdentification_user())
                            .totalPayment(tolalPay)
                            .totalQuantity(totalQua)
                            .cart_items(boxItem)
                            .build()
            );
        }
        return save;
    }

    public CartResponse getCartById(Integer id) {
        var flag = cartRepository.findById(id);
        var tolalPay = 0.0d;
        var totalQua = 0;
        List<SubCart_OrderResponse> boxItem = new ArrayList<>();
        var detail = cartDetailRepository.findByIdentification_cart(flag.get().getId());
        for(var y : detail){
            var itemCart = SubCart_OrderResponse.builder()
                    .itemDetail_id(y.getId())
                    .name(y.getName())
                    .size(y.getSize())
                    .color(y.getColor())
                    .image(y.getImage())
                    .quantity(y.getQuantity())
                    .unitPrice(y.getUnitPrice())
                    .totalPrice(y.getTotalPrice())
                    .build();
            boxItem.add(itemCart);
            tolalPay += y.getTotalPrice();
            totalQua += y.getQuantity();
        }
        return CartResponse.builder()
                        .cart_id(flag.get().getId())
                        .user_id(flag.get().getIdentification_user())
                        .totalPayment(tolalPay)
                        .totalQuantity(totalQua)
                        .cart_items(boxItem)
                        .build();
    }

    public CartResponse getCartByUserId(Integer user_id) {
        var flag = cartRepository.findByIdentification(user_id);
        var tolalPay = 0.0d;
        var totalQua = 0;
        List<SubCart_OrderResponse> boxItem = new ArrayList<>();
        var detail = cartDetailRepository.findByIdentification_cart(flag.getId());
        for(var y : detail){
            var itemCart = SubCart_OrderResponse.builder()
                    .itemDetail_id(y.getId())
                    .name(y.getName())
                    .size(y.getSize())
                    .color(y.getColor())
                    .image(y.getImage())
                    .quantity(y.getQuantity())
                    .unitPrice(y.getUnitPrice())
                    .totalPrice(y.getTotalPrice())
                    .build();
            boxItem.add(itemCart);
            tolalPay += y.getTotalPrice();
            totalQua += y.getQuantity();
        }
        return CartResponse.builder()
                .cart_id(flag.getId())
                .user_id(flag.getIdentification_user())
                .totalPayment(tolalPay)
                .totalQuantity(totalQua)
                .cart_items(boxItem)
                .build();
    }
    @Transactional
    public CartResponse updateInfoItemCart(CartDtos request) {
        var user = userRepository.findById_create(request.getUserId());
        if(user == null){
            return null;
        }
        var totalPayment = 0.0d;
        var totalQuantity = 0;
        var cart_user= cartRepository.findByIdentification(user.getId());
        var boxItem_user = cartDetailRepository.findByIdentification_cart(cart_user.getId());
        System.out.println(boxItem_user.size());
        System.out.println(request.getCart_items().size());
        List<SubCart_OrderResponse> boxItem = new ArrayList<>();
        for (var x : request.getCart_items()){
            for(var y : boxItem_user){
                if(
                        Objects.equals(y.getName(), x.getName()) &&
                        Objects.equals(y.getColor(), x.getColor()) &&
                        Objects.equals(y.getSize(), x.getSize())
                ){
                    y.setQuantity(x.getQuantity());
                    y.setUnitPrice(x.getUnitPrice());
                    y.setTotalPrice(x.getTotalPrice());
                    totalPayment += x.getTotalPrice();
                    totalQuantity += x.getQuantity();
                    cartDetailRepository.save(y);
                    var itemCart = SubCart_OrderResponse.builder()
                            .itemDetail_id(y.getId())
                            .name(y.getName())
                            .size(y.getSize())
                            .color(y.getColor())
                            .image(y.getImage())
                            .quantity(y.getQuantity())
                            .unitPrice(y.getUnitPrice())
                            .totalPrice(y.getTotalPrice())
                            .build();
                    boxItem.add(itemCart);
                }
            }
        }
        return CartResponse.builder()
                .cart_id(cart_user.getId())
                .user_id(cart_user.getIdentification_user())
                .totalQuantity(totalQuantity)
                .totalPayment(totalPayment)
                .cart_items(boxItem)
                .build();
    }

    @Transactional
    public CartResponse createOrAddCart(CartDetailDtos request) {
        Double totalPay = 0.0d;
        var totalQua = 0;
        var user = userRepository.findById_create(request.getUserId());
        if(user == null){
            return null;
        }
        var cart_user= cartRepository.findByIdentification(user.getId());
        var boxItem_user = cartDetailRepository.findByIdentification_cart(cart_user.getId());
        List<CartDetail> box = new ArrayList<>();
        List<SubCart_OrderResponse> boxItem = new ArrayList<>();
        if(boxItem_user.size() == 0){
            var additem = CartDetail.builder()
                    .identification_cart(cart_user.getId())
                    .product_identification(request.getCart_item().getItemDetail_id())
                    .name(request.getCart_item().getName())
                    .color(request.getCart_item().getColor())
                    .size(request.getCart_item().getSize())
                    .image(request.getCart_item().getImage())
                    .unitPrice(request.getCart_item().getUnitPrice())
                    .totalPrice(request.getCart_item().getTotalPrice())
                    .quantity(request.getCart_item().getQuantity())
                    .build();
            var additem_0 = cartDetailRepository.save(additem);
            totalPay += additem_0.getTotalPrice();
            totalQua += additem_0.getQuantity();
            var itemCart = SubCart_OrderResponse.builder()
                    .itemDetail_id(additem_0.getProduct_identification())
                    .name(additem_0.getName())
                    .size(additem_0.getSize())
                    .color(additem_0.getColor())
                    .image(additem.getImage())
                    .quantity(additem.getQuantity())
                    .unitPrice(additem_0.getUnitPrice())
                    .totalPrice(additem.getTotalPrice())
                    .build();
            box.add(additem_0);
            boxItem.add(itemCart);
            cart_user.setCartDetails(box);
            cartRepository.save(cart_user);

            return CartResponse.builder()
                    .cart_id(additem_0.getIdentification_cart())
                    .user_id(user.getId())
                    .totalPayment(totalPay)
                    .totalQuantity(totalQua)
                    .cart_items(boxItem)
                    .build();
        }else {
            var check_var = false;
            box = cartDetailRepository.findByIdentification_cart(cart_user.getId());
            var additem = CartDetail.builder()
                    .identification_cart(cart_user.getId())
                    .product_identification(request.getCart_item().getItemDetail_id())
                    .name(request.getCart_item().getName())
                    .color(request.getCart_item().getColor())
                    .size(request.getCart_item().getSize())
                    .image(request.getCart_item().getImage())
                    .unitPrice(request.getCart_item().getUnitPrice())
                    .totalPrice(request.getCart_item().getTotalPrice())
                    .quantity(request.getCart_item().getQuantity())
                    .build();
            for(var x : box){
                var itemCart = SubCart_OrderResponse.builder()
                        .itemDetail_id(x.getProduct_identification())
                        .name(x.getName())
                        .size(x.getSize())
                        .color(x.getColor())
                        .image(x.getImage())
                        .quantity(x.getQuantity())
                        .unitPrice(x.getUnitPrice())
                        .totalPrice(x.getTotalPrice())
                        .build();
                if(    Objects.equals(x.getProduct_identification(), request.getCart_item().getItemDetail_id()) &&
                        Objects.equals(x.getName(), request.getCart_item().getName()) &&
                        Objects.equals(x.getColor(), request.getCart_item().getColor()) &&
                        Objects.equals(x.getSize(), request.getCart_item().getSize())
                ){
                    var newQuantity = x.getQuantity()+additem.getQuantity();
                    var newTotalPayment = x.getUnitPrice()*newQuantity;
                    x.setQuantity(newQuantity);
                    x.setTotalPrice(newTotalPayment);
                    cartDetailRepository.save(x);
                    totalQua += newQuantity;
                    totalPay += newTotalPayment;
                    itemCart.setQuantity(newQuantity);
                    itemCart.setTotalPrice(newTotalPayment);
                    boxItem.add(itemCart);
                    check_var = true;
                }else {
                    totalQua += x.getQuantity();
                    totalPay += x.getTotalPrice();
                    boxItem.add(itemCart);
                }
            }
            if(!check_var){
                var itemCart = SubCart_OrderResponse.builder()
                        .itemDetail_id(additem.getProduct_identification())
                        .name(additem.getName())
                        .size(additem.getSize())
                        .color(additem.getColor())
                        .image(additem.getImage())
                        .quantity(additem.getQuantity())
                        .unitPrice(additem.getUnitPrice())
                        .totalPrice(additem.getTotalPrice())
                        .build();
                totalQua += additem.getQuantity();
                totalPay += additem.getTotalPrice();
                boxItem.add(itemCart);
                cartDetailRepository.save(additem);
            }
            box = cartDetailRepository.findByIdentification_cart(cart_user.getId());
//            for(var x : box){
//                if(!x.get.contains())
//            }
            cart_user.setCartDetails(box);
            cartRepository.save(cart_user);
            return CartResponse.builder()
                    .cart_id(cart_user.getId())
                    .totalPayment(totalPay)
                    .totalQuantity(totalQua)
                    .user_id(user.getId())
                    .cart_items(boxItem)
                    .build();
        }
    }


    public ResponseMessageDtos makeEmptyCart(Integer id) {
        Boolean b = cartRepository.findById(id).map(cart -> {
            cartDetailRepository.deleteByIdentificationCart(id);
            return true;
        }).orElse(false);
        if(b){
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Make_Empty_Cart_Success)
                    .build();
        }
        return ResponseMessageDtos.builder()
                .status(404)
                .message(Constants.Make_Empty_Cart_Fail)
                .build();
    }


    public ResponseMessageDtos deleteCartItemDetail(Integer detailId) {
        Boolean b = cartDetailRepository.findById(detailId).map(cartDetail -> {
            cartDetailRepository.delete(cartDetail);
            return true;
        }).orElse(false);
        if(b){
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Delete_Cart_Item_Success)
                    .build();
        }
        return ResponseMessageDtos.builder()
                .status(404)
                .message(Constants.Delete_Cart_Item_Fail)
                .build();
    }
}
