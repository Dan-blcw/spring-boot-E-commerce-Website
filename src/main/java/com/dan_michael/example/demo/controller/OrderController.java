package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.model.entities.SubEn.OrderDetail;
import com.dan_michael.example.demo.service.OrderService;
import com.dan_michael.example.demo.util.Constants;
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

//--------------------------Order(CRUD)----------------------------------
    private final OrderService orderService;

    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
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
        return ResponseEntity.ok(response);
    }


    // Update an existing order
    @PutMapping(value = "/{id}",consumes = { "application/json"})
    public ResponseEntity<Order> updateOrder(
            @PathVariable Integer id,
            @RequestBody OrderDtos orderDetails
    ) {
        Optional<Order> updatedOrder = orderService.updateOrder(id, orderDetails);
        return updatedOrder.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
        boolean isDeleted = orderService.deleteOrderAndOrderDetail(id);
        return isDeleted ? ResponseEntity.ok()
                .body(ResponseMessageDtos.builder()
                        .message(Constants.Delete_Cart_Item_Success)
                        .status(200).build()
                ) : ResponseEntity.notFound().build();
    }

//--------------------------Order Detail----------------------------------
    @GetMapping("/order-detail/{id}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@PathVariable Integer id) {
        List<OrderDetail> order = orderService.getOrderDetailsByOrderId(id);
        return ResponseEntity.ok(order);
    }
    @DeleteMapping("/order-detail/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable Integer id) {
        boolean isDeleted = orderService.deleteOrderDetail(id);
        return isDeleted ? ResponseEntity.ok().body(
                ResponseMessageDtos.builder()
                        .message(Constants.Delete_Order_Detail_Success)
                        .status(200).build()
        ) : ResponseEntity.notFound().build();
    }
}
