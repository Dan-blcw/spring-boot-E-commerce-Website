package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.CartDtos;
import com.dan_michael.example.demo.model.entities.*;
import com.dan_michael.example.demo.model.entities.SubEn.CartDetail;
import com.dan_michael.example.demo.model.response.CartResponse;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.response.SubCart_OrderResponse;
import com.dan_michael.example.demo.repositories.SupRe.CartDetailRepository;
import com.dan_michael.example.demo.repositories.CartRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
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

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

//---------------------------Cart & cartDetail---------------------------------------
    public List<CartResponse> getAllCarts() {
        List<CartResponse> save = new ArrayList<>();
        var flag = cartRepository.findAll();
        for (var x: flag){
            var tolalPay = 0.0f;
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
                            .cartDetails(boxItem)
                            .build()
            );
        }
        return save;
    }

    public CartResponse getCartById(Integer id) {
        var flag = cartRepository.findById(id);
        var tolalPay = 0.0f;
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
                        .cartDetails(boxItem)
                        .build();
    }

    public CartResponse getCartByUserId(Integer user_id) {
        var flag = cartRepository.findByIdentification(user_id);
        var tolalPay = 0.0f;
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
                .cartDetails(boxItem)
                .build();
    }
    @Transactional
    public SubCart_OrderResponse updateQuantityItemCart(CartDtos request) {
        var user = userRepository.findById_create(request.getUserId());
        if(user == null){
            return null;
        }
        var cart_user= cartRepository.findByIdentification(user.getId());
        List<SubCart_OrderResponse> boxItem = new ArrayList<>();
        var box = cartDetailRepository.findByIdentification_cart_(
                cart_user.getId(),
                request.getCart_item().getColor(),
                request.getCart_item().getSize()
        );
        box.setQuantity(request.getCart_item().getQuantity());
        box.setUnitPrice(request.getCart_item().getUnitPrice());
        box.setTotalPrice(request.getCart_item().getTotalPrice());
        cartDetailRepository.save(box);
        cartRepository.save(cart_user);
        return SubCart_OrderResponse.builder()
                .itemDetail_id(box.getProduct_identification())
                .name(box.getName())
                .quantity(box.getQuantity())
                .image(box.getImage())
                .size(box.getSize())
                .color(box.getColor())
                .unitPrice(box.getUnitPrice())
                .totalPrice(box.getTotalPrice())
                .build();
    }

    @Transactional
    public CartResponse createOrAddCart(CartDtos request) {
        Float totalPay = 0.0f;
        var totalQua = 0;
        var user = userRepository.findById_create(request.getUserId());
        if(user == null){
            return null;
        }
        var cart_user= cartRepository.findByIdentification(user.getId());
        List<CartDetail> box = new ArrayList<>();
        List<SubCart_OrderResponse> boxItem = new ArrayList<>();
        if(cart_user == null){
            System.out.println("Cart Not Null");
            Cart flag = new Cart();
            flag.setIdentification_user(user.getId());
            flag.setCreatedAt(new Date());
            cartRepository.save(flag).getId();
            box = cartDetailRepository.findByIdentification_cart(flag.getId());
            var additem = CartDetail.builder()
                    .identification_cart(flag.getId())
                    .product_identification(request.getCart_item().getProduct_id())
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
                    .itemDetail_id(additem_0.getId())
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
            flag.setCartDetails(box);
            cartRepository.save(flag);

            return CartResponse.builder()
                    .cart_id(additem_0.getIdentification_cart())
                    .user_id(user.getId())
                    .totalPayment(totalPay)
                    .totalQuantity(totalQua)
                    .cartDetails(boxItem)
                    .build();
        }else {
            System.out.println("Cart Detail Not Null");
            var check_trung = false;
            box = cartDetailRepository.findByIdentification_cart(cart_user.getId());
            var additem = CartDetail.builder()
                    .identification_cart(cart_user.getId())
                    .product_identification(request.getCart_item().getProduct_id())
                    .name(request.getCart_item().getName())
                    .color(request.getCart_item().getColor())
                    .size(request.getCart_item().getSize())
                    .image(request.getCart_item().getImage())
                    .unitPrice(request.getCart_item().getUnitPrice())
                    .totalPrice(request.getCart_item().getTotalPrice())
                    .quantity(request.getCart_item().getQuantity())
                    .build();
            for(var x : box){
                if(     Objects.equals(x.getProduct_identification(), additem.getProduct_identification()) &&
                        Objects.equals(x.getName(), additem.getName()) &&
                        Objects.equals(x.getColor(), additem.getColor()) &&
                        Objects.equals(x.getSize(), additem.getSize()) &&
                        Objects.equals(x.getProduct_identification(), additem.getProduct_identification())){
                    x.setQuantity(x.getQuantity()+additem.getQuantity());
                    totalPay += x.getTotalPrice();
                    totalQua += x.getQuantity();
                    cartDetailRepository.save(x);
                    check_trung = true;
                    break;
                }
                totalQua += x.getQuantity();
            }
            for (var x: box) {
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
                boxItem.add(itemCart);
                totalPay += x.getTotalPrice();
            }
            if(!check_trung){
                cartDetailRepository.save(additem);
            }
            cart_user.setCartDetails(box);
            cartRepository.save(cart_user);
            return CartResponse.builder()
                    .cart_id(cart_user.getId())
                    .totalPayment(totalPay)
                    .totalQuantity(totalQua)
                    .user_id(user.getId())
                    .cartDetails(boxItem)
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
