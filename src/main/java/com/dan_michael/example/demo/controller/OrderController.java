package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.ResponseMessageDtos;
import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dan_michael.example.demo.model.dto.ob.OrderDtos;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

//--------------------------Read Only Product----------------------------------



    private final OrderService orderService;

    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get order by id
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//     Create a new order
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderDtos request
    ) {
        Order response = orderService.createOrder(request);
        if(response != null){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(ResponseMessageDtos.builder().message("This Category already exist").status(400).build());
        }
    }


    // Update an existing order
    @PutMapping(value = "/{id}",consumes = { "application/json"})
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody OrderDtos orderDetails) {
        Optional<Order> updatedOrder = orderService.updateOrder(id, orderDetails);
        return updatedOrder.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        boolean isDeleted = orderService.deleteOrderAndOrderDetail(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/order-detail/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable Integer id) {
        boolean isDeleted = orderService.deleteOrderDetail(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
