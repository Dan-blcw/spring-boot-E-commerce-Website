package com.dan_michael.example.demo.controller;
import com.dan_michael.example.demo.model.dto.global.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CartDtos;
import com.dan_michael.example.demo.model.dto.ob.OrderDtos;
import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import com.dan_michael.example.demo.model.entities.Cart;
import com.dan_michael.example.demo.model.entities.CartDetail;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    //     Create a new order
//    @PostMapping
//    public ResponseEntity<?> createCart(
//            @RequestBody CartDtos request
//    ) {
//        var response = cartService.createCartItem(request);
//        if(response != null){
//            return ResponseEntity.ok(response);
//        }else {
//            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This Category already exist").status(400).build());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCart(@PathVariable Integer id) {
//        cartService.deleteCart(id);
//        return ResponseEntity.noContent().build();
//    }

    // Other controller methods

//
    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Integer id) {
        Optional<Cart> cart = cartService.getCartById(id);
        if (cart.isPresent()) {
            return ResponseEntity.ok(cart.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCart(
            @RequestBody CartDtos request
    ) {
        Cart response = cartService.createCart(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This Category already exist").status(400).build());
        }
    }

    @PutMapping("/{id}/details")
    public ResponseEntity<Cart> addCartDetail(@PathVariable Integer id, @RequestBody CartDtos cartDetails) {
        Optional<Cart> updatedCart = cartService.addCartDetail(id, cartDetails);
        if (updatedCart.isPresent()) {
            return ResponseEntity.ok(updatedCart.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Integer id) {
        if (cartService.deleteCart(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/details/{detailId}")
    public ResponseEntity<Void> deleteCartDetail(@PathVariable Integer detailId) {
        if (cartService.deleteCartDetail(detailId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}
