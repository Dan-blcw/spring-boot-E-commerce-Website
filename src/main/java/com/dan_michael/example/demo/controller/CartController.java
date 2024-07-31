package com.dan_michael.example.demo.controller;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CartDtos;
import com.dan_michael.example.demo.service.CartService;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import com.dan_michael.example.demo.model.entities.Cart;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;
    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/detail-cart/{cart_id}")
    public ResponseEntity<?> getCartByCart_Id(
            @PathVariable Integer cart_id
    ) {
        Optional<Cart> cart = cartService.getCartById(cart_id);
        if (cart.isPresent()) {
            return ResponseEntity.ok(cart.get());
        }
        return ResponseEntity
                .status(404)
                .body(Constants.Cart_Empty);
    }

    @GetMapping("/detail-cart")
    public ResponseEntity<?> getCartByUser_Id(
            @RequestParam Integer user_id
    ) {
        Cart cart = cartService.getCartByUserId(user_id);
        if (cart !=null) {
            return ResponseEntity.ok(cart);
        }
        return ResponseEntity
                .status(404)
                .body(Constants.Cart_Empty);
    }


    @PostMapping("/add-details")
    public ResponseEntity<?> createCartOrAdd(
            @RequestBody CartDtos cartDetails) {
        var cart = cartService.createOrAddCart(cartDetails);
        if (cart !=null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity
                    .status(404)
                    .body(Constants.Add_Cart_Fail);
        }
    }

    @PostMapping("/update-item-details")
    public ResponseEntity<?> UpdateInforItemDetai(
            @RequestBody CartDtos cartDetails) {
        var cart = cartService.updateQuantityItemCart(cartDetails);
        if (cart !=null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity
                    .status(404)
                    .body(Constants.Update_Cart_Fail);
        }
    }
    @DeleteMapping("/make-empty/{id}")
    public ResponseMessageDtos deleteCart(@PathVariable Integer id) {
        return cartService.makeEmptyCart(id);
    }
    @DeleteMapping("/detail-cart/{detail_Id}")
    public ResponseMessageDtos deleteCartDetail(@PathVariable Integer detail_Id) {
        return cartService.deleteCartItemDetail(detail_Id);
    }
}
