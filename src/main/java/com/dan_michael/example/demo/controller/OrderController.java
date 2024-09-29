package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.entities.Transaction;
import com.dan_michael.example.demo.model.response.OrderResponse;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.model.entities.SubEn.OrderDetail;
import com.dan_michael.example.demo.repositories.OrderRepository;
import com.dan_michael.example.demo.repositories.QRInfoRepository;
import com.dan_michael.example.demo.repositories.TransactionRepository;
import com.dan_michael.example.demo.service.OrderService;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dan_michael.example.demo.model.dto.ob.OrderDtos;

import java.util.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

//--------------------------Clone hoa don----------------------------------

//--------------------------Order(CRUD)----------------------------------
    private final OrderService orderService;

    private final OrderRepository orderRepository;

    private final QRInfoRepository qrInfoRepository;

    private final TransactionRepository transactionRepository;

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
        var newTransaction = Transaction.builder().build();
        if(Objects.equals(request.getPaymentMethods(), "Quét Mã QR")){
             var qrInfos = qrInfoRepository.findQRInfoActive();
             if(qrInfos !=null){
                 newTransaction = Transaction.builder()
                         .accountNo(qrInfos.get(0).getAccountNo())
                         .accountName(qrInfos.get(0).getAccountName())
                         .acqId(qrInfos.get(0).getAcqId())
                         .template(qrInfos.get(0).getTemplate())
                         .skuOrder(response.getSkuOrder())
                         .addInfo("Thanh toán cho mã hóa đơn " + response.getSkuOrder())
                         .amount(String.valueOf(response.getTotalPayment()))
                         .paymentMethods(response.getPaymentMethods())
                         .paymentStatus(response.getPaymentStatus())
                         .transactionDate(new Date())
                         .build();
                 transactionRepository.save(newTransaction);
             }else {
                 return ResponseEntity.ok(ResponseMessageDtos.builder()
                                 .status(400)
                                 .message("Hiện tại không có tài khản ngân hàng quét QR khả dụng !")
                         .build());
             }
        }else if(Objects.equals(request.getPaymentMethods(), "VNPay")){
             newTransaction = Transaction.builder()
                    .accountNo("Liên kết VNPay")
                    .accountName("Liên kết VNPay")
                    .acqId("Liên kết VNPay")
                    .template("Liên kết VNPay")
                    .skuOrder(response.getSkuOrder())
                    .addInfo("Thanh toán cho mã hóa đơn " + response.getSkuOrder())
                    .amount(String.valueOf(response.getTotalPayment()))
                    .paymentMethods(response.getPaymentMethods())
                    .paymentStatus(response.getPaymentStatus())
                    .transactionDate(new Date())
                    .build();
            transactionRepository.save(newTransaction);
        }else {
            newTransaction = Transaction.builder()
                    .accountNo("")
                    .accountName("")
                    .acqId("")
                    .template("")
                    .skuOrder(response.getSkuOrder())
                    .addInfo("Thanh toán cho mã hóa đơn " + response.getSkuOrder())
                    .amount(String.valueOf(response.getTotalPayment()))
                    .paymentMethods(response.getPaymentMethods())
                    .paymentStatus(response.getPaymentStatus())
                    .transactionDate(new Date())
                    .build();
            transactionRepository.save(newTransaction);
        }
        return ResponseEntity.ok(newTransaction);
//        return ResponseEntity.ok(response);
    }

    // Update an existing order
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/admin/{id}",consumes = { "application/json"})
    public ResponseEntity<Order> updateOrderAdmin(
            @PathVariable Integer id,
            @RequestBody OrderDtos orderDetails
    ) {
        Optional<Order> updatedOrder = orderService.updateOrderAdmin(id, orderDetails);
        if(updatedOrder.isPresent()){
            var saveTran = transactionRepository.findTransactionBySkuOrder(updatedOrder.get().getSkuOrder());
            saveTran.setPaymentMethods(updatedOrder.get().getPaymentMethods());
            saveTran.setPaymentStatus(updatedOrder.get().getPaymentStatus());
            transactionRepository.save(saveTran);
        }
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
