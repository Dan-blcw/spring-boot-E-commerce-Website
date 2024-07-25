package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.CartDtos;
import com.dan_michael.example.demo.model.dto.ob.OrderDtos;
import com.dan_michael.example.demo.model.entities.*;
import com.dan_michael.example.demo.repositories.CartDetailRepository;
import com.dan_michael.example.demo.repositories.CartRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartDetailRepository cartDetailRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;
//---------------------------Cart---------------------------------------
//    @Transactional
//    public void deleteCart(Integer cartId) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
//        List<CartDetail> items = cartDetailRepository.findByCart(cart);
//        cartDetailRepository.deleteAll(items);
//        cartRepository.delete(cart);
//    }
//---------------------------cartDetail---------------------------------------

//    @Transactional
//    public CartDetail createCartItem(CartDtos cartDtos) {
//        Product product = productRepository.findById(cartDtos.getCart_item().getProduct_id())
//                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
//
//        if (cartDtos.getCart_item().getQuantity() > product.getQuantity()) {
//            throw new IllegalArgumentException("Quantity in stock is not enough");
//        }
//
//        Cart cart = cartRepository.findByUser(cartDtos.getUser());
//
//        List<CartDetail> cartItems = cartDetailRepository.findByCartAndProductAndColorsAndSize(cart, product, cartDtos.getCart_item().getColors(), cartDtos.getCart_item().getSize());
//        if (!cartItems.isEmpty()) {
//            CartDetail existingCartItem = cartItems.get(0);
//            int newQuantity = existingCartItem.getQuantity() + cartDtos.getCart_item().getQuantity();
//            if (newQuantity > product.getQuantity()) {
//                throw new IllegalArgumentException("Quantity in stock is not enough");
//            }
//            existingCartItem.setQuantity(newQuantity);
//            return cartDetailRepository.save(existingCartItem);
//        }
//
//        CartDetail cartItem = new CartDetail();
//        cartItem.setCart(cart);
//        cartItem.setProduct(product);
//        cartItem.setColors(cartDtos.getCart_item().getColors());
//        cartItem.setSize(cartDtos.getCart_item().getSize());
//        cartItem.setQuantity(cartItem.getQuantity());
//        return cartDetailRepository.save(cartItem);
//    }


//---------------------------cartDetail---------------------------------------
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
    @Transactional
    public Cart createCart(CartDtos request) {
        var user = userRepository.findById_create(request.getUserId());
        if(user == null){
            return new Cart();
        }
        Cart flag = new Cart();

        flag.setIdentification_user(user.getId());
        flag.setCreatedAt(new Date());
        var y = cartRepository.save(flag);
        var item = CartDetail.builder()
                .identification_product(request.getCart_item().getProduct_id())
                .size(request.getCart_item().getSize())
                .quantity(request.getCart_item().getQuantity())
                .identification_cart(y.getId())
                .build();
        List<CartDetail> box = new ArrayList<>();
        box.add(item);
        cartDetailRepository.save(item);
        y.setCartDetails(box);
        return y;
    }

    public Optional<Cart> addCartDetail(Integer id, CartDtos request) {
        var flag = cartRepository.findById(id);
        var box = cartDetailRepository.findByIdentification_cart(id);
        return cartRepository.findById(id).map(cart -> {
//          Tạo detailItem
            var additem = CartDetail.builder()
                    .identification_cart(id)
                    .identification_product(request.getCart_item().getProduct_id())
                    .colors(request.getCart_item().getColors())
                    .size(request.getCart_item().getSize())
                    .quantity(request.getCart_item().getQuantity())
                    .build();
            cart.setIdentification_user(request.getUserId());
            var additem_0 = cartDetailRepository.save(additem);
            box.add(additem_0);
            cart.setCartDetails(box);
            return cartRepository.save(cart);
        });
    }




    public boolean deleteCart(Integer id) {
        return cartRepository.findById(id).map(cart -> {
            cartRepository.delete(cart);
            cartDetailRepository.deleteByIdentificationCart(id);
            return true;
        }).orElse(false);
    }


    public boolean deleteCartDetail(Integer detailId) {
        return cartDetailRepository.findById(detailId).map(cartDetail -> {
            cartDetailRepository.delete(cartDetail);
            return true;
        }).orElse(false);
    }
}
