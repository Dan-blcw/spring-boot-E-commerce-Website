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
    public List<Cart> getAllCarts() {
        var flag = cartRepository.findAll();
        flag.forEach(x->x.setCartDetails(cartDetailRepository.findByIdentification_cart(x.getId())));
        return flag;
    }

    public Optional<Cart> getCartById(Integer id) {
        var flag = cartRepository.findById(id);
        flag.get().setCartDetails(cartDetailRepository.findByIdentification_cart(id));
        return flag;
    }

    public Cart getCartByUserId(Integer user_id) {
        var cart_user = cartRepository.findByIdentification(user_id);
        cart_user.setCartDetails(cartDetailRepository.findByIdentification_cart(cart_user.getId()));
        return cart_user;
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
        cartDetailRepository.save(box);
        cartRepository.save(cart_user);
        return SubCart_OrderResponse.builder()
                .product_id(box.getIdentification_product())
                .quantity(box.getQuantity())
                .size(box.getSize())
                .color(box.getColor())
                .build();
    }

    @Transactional
    public CartResponse createOrAddCart(CartDtos request) {
        var user = userRepository.findById_create(request.getUserId());
        if(user == null){
            return null;
        }
        var cart_user= cartRepository.findByIdentification(user.getId());
        List<CartDetail> box = new ArrayList<>();
        List<SubCart_OrderResponse> boxItem = new ArrayList<>();
        if(cart_user == null){
            Cart flag = new Cart();
            flag.setIdentification_user(user.getId());
            flag.setCreatedAt(new Date());
            cartRepository.save(flag).getId();
            box = cartDetailRepository.findByIdentification_cart(flag.getId());
            var additem = CartDetail.builder()
                    .identification_cart(flag.getId())
                    .identification_product(request.getCart_item().getProduct_id())
                    .color(request.getCart_item().getColor())
                    .size(request.getCart_item().getSize())
                    .subTotal(request.getCart_item().getSubTotal())
                    .quantity(request.getCart_item().getQuantity())
                    .build();
            var additem_0 = cartDetailRepository.save(additem);
            var itemCart = SubCart_OrderResponse.builder()
                    .itemDetail_id(additem_0.getId())
                    .product_id(additem_0.getIdentification_product())
                    .size(additem_0.getSize())
                    .color(additem_0.getColor())
                    .quantity(additem.getQuantity())
                    .subTotal(additem_0.getSubTotal())
                    .build();
            box.add(additem_0);
            boxItem.add(itemCart);
            flag.setCartDetails(box);
            cartRepository.save(flag);

            return CartResponse.builder()
                    .cart_id(additem_0.getIdentification_cart())
                    .user_id(user.getId())
                    .cartDetails(boxItem)
                    .build();
        }else {
            box = cartDetailRepository.findByIdentification_cart(cart_user.getId());
            for(var x : box){
                var additem = CartDetail.builder()
                        .identification_cart(cart_user.getId())
                        .identification_product(request.getCart_item().getProduct_id())
                        .color(request.getCart_item().getColor())
                        .size(request.getCart_item().getSize())
                        .subTotal(request.getCart_item().getSubTotal())
                        .quantity(request.getCart_item().getQuantity())
                        .build();
                if(Objects.equals(x.getColor(), additem.getColor()) &&
                        Objects.equals(x.getSize(), additem.getSize()) &&
                        Objects.equals(x.getIdentification_product(), additem.getIdentification_product())){
                    x.setQuantity(x.getQuantity()+additem.getQuantity());
                    cartDetailRepository.save(x);
                }
            }
            for (var x: box) {
                var itemCart = SubCart_OrderResponse.builder()
                        .itemDetail_id(x.getId())
                        .product_id(x.getIdentification_product())
                        .size(x.getSize())
                        .color(x.getColor())
                        .quantity(x.getQuantity())
                        .subTotal(x.getSubTotal())
                        .build();
                boxItem.add(itemCart);
            }
            cart_user.setCartDetails(box);
            cartRepository.save(cart_user);
            return CartResponse.builder()
                    .cart_id(cart_user.getId())
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
