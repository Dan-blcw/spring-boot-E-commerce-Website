package com.dan_michael.example.demo.controller;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CartDtos;
import com.dan_michael.example.demo.service.CartService;
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

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Integer id) {
        Optional<Cart> cart = cartService.getCartById(id);
        if (cart.isPresent()) {
            return ResponseEntity.ok(cart.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/add-details")
    public ResponseEntity<Cart> createCartAndadd(
            @RequestBody CartDtos cartDetails) {
        var id = cartService.createOrGetCart(cartDetails);
        Optional<Cart> updatedCart = cartService.addCartDetail(id, cartDetails);
        if (updatedCart.isPresent()) {
            return ResponseEntity.ok(updatedCart.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Integer id) {
        if (cartService.deleteCart(id)) {
            return ResponseEntity.ok().body("Delete Cart Successfully !!!");
        } else {
            return ResponseEntity.status(404).body("Delete Cart Failure !!!");
        }
    }

    @DeleteMapping("/details/{detail_Id}")
    public ResponseEntity<?> deleteCartDetail(@PathVariable Integer detail_Id) {
        if (cartService.deleteCartDetail(detail_Id)) {
            return ResponseEntity.ok().body("Delete Cart Successfully !!!");
        } else {
            return ResponseEntity.status(404).body("Delete Cart Item Failure !!!");
        }
    }
}
