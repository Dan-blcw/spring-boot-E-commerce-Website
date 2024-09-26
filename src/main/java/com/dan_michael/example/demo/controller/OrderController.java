package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.response.OrderResponse;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.model.entities.SubEn.OrderDetail;
import com.dan_michael.example.demo.service.OrderService;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dan_michael.example.demo.model.dto.ob.OrderDtos;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

//--------------------------Clone hoa don----------------------------------
//var createdAt = x.getCreatedAt();
//    Calendar calendar = Calendar.getInstance();
//            calendar.setTime(createdAt);
//    int month = calendar.get(Calendar.MONTH);
//            switch (month) {
//        case 0 -> nt1 += x.getTotalAmountOrder();
//        case 1 -> nt2 += x.getTotalAmountOrder();
//        case 2 -> nt3 += x.getTotalAmountOrder();
//        case 3 -> nt4 += x.getTotalAmountOrder();
//        case 4 -> nt5 += x.getTotalAmountOrder();
//        case 5 -> nt6 += x.getTotalAmountOrder();
//        case 6 -> nt7 += x.getTotalAmountOrder();
//        case 7 -> nt8 += x.getTotalAmountOrder();
//        case 8 -> nt9 += x.getTotalAmountOrder();
//        case 9 -> nt10 += x.getTotalAmountOrder();
//        case 10 -> nt11 += x.getTotalAmountOrder();
//        case 11 -> nt12 += x.getTotalAmountOrder();
//        default -> {
//        }
//    }

//--------------------------Order(CRUD)----------------------------------
    private final OrderService orderService;

    // Get all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/classify")
    public List<Order> getAllOrdersByOrderStatus(
            @RequestParam(required = false) String orderStatus
    ) {
        return orderService.getAllOrdersByOrderStatus(orderStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}")
    public List<Order> getOrderByUserId(@PathVariable Integer id) {
        return orderService.getOrderByUserId(id);
    }

    @GetMapping("/user/classify")
    public List<Order> getOrderByUserIdClassify(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer paymentStatus,
            @RequestParam(required = false) String orderStatus
    ) {
        if(paymentStatus != null  && orderStatus == null){
            return orderService.getOrderByUserId_PaymentStatus(userId,paymentStatus);
        }
        if(orderStatus!= null  && paymentStatus == null){
            return orderService.getOrderByUserId_OrderStatus(userId,orderStatus);
        }
        return orderService.getOrderByUserId_Both(userId,paymentStatus,orderStatus);
    }
//     Create a new order
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderDtos request
    ) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }

    // Update an existing order
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/admin/{id}",consumes = { "application/json"})
    public ResponseEntity<Order> updateOrderAdmin(
            @PathVariable Integer id,
            @RequestBody OrderDtos orderDetails
    ) {
        Optional<Order> updatedOrder = orderService.updateOrderAdmin(id, orderDetails);
        return updatedOrder.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/admin-clone-time/{id}",consumes = { "application/json"})
    public ResponseEntity<Order> CloneTimeOrder(
            @PathVariable Integer id,
            @RequestBody OrderDtos orderDetails
    ) {
        Optional<Order> updatedOrder = orderService.CloneTime(id, orderDetails);
        return updatedOrder.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}",consumes = { "application/json"})
    public ResponseEntity<Order> updateOrderUser(
            @PathVariable Integer id,
            @RequestBody OrderDtos orderDetails
    ) {
        Optional<Order> updatedOrder = orderService.updateOrderUser(id, orderDetails);
        return updatedOrder.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update an existing order
//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/canceled-order/{id}",consumes = { "application/json"})
    public ResponseEntity<ResponseMessageDtos> CanceledOrder(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(orderService.CanceledOrder(id));
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
