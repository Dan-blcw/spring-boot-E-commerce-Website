package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.OrderDtos;
import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.model.entities.OrderDetail;
import com.dan_michael.example.demo.repositories.OrderDetailRepository;
import com.dan_michael.example.demo.repositories.OrderRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.dan_michael.example.demo.model.dto.ob.ItemDetailDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

//--------------------------Order----------------------------------
    public List<Order> getAllOrders() {
        var flag = orderRepository.findAll();
        flag.forEach(x->x.setOrderDetails(orderDetailRepository.findByIdentification_order(x.getId())));
        return flag;
    }

    public Optional<Order> getOrderById(Integer id) {
        var flag = orderRepository.findById(id);
        flag.get().setOrderDetails(orderDetailRepository.findByIdentification_order(id));
        return flag;
    }

    @Transactional
    public Order createOrder(OrderDtos request) {
        var user = userRepository.findById_create(request.getUserId());
        Order flag = new Order();

        flag.setIdentification_user(user.getId());
        flag.setAddress(request.getAddress());
        flag.setCompanyName(request.getCompanyName());
        flag.setPhoneNumber(request.getPhoneNumber());
        flag.setEmailAddress(request.getEmailAddress());
        flag.setOrderStatus(request.getOrderStatus());
        flag.setShippingStatus("ĐANG CHUẨN BỊ HÀNG");
        flag.setCreatedAt(new Date());
        var y = orderRepository.save(flag);
        List<OrderDetail> box = new ArrayList<>();
        for (ItemDetailDto x : request.getOrderDetails()) {
            OrderDetail flag_1 = new OrderDetail();
            flag_1.setProduct_id(x.getProduct_id());
            flag_1.setQuantity(x.getQuantity());
            flag_1.setIdentification_order(y.getId());
            box.add(flag_1);
            createOrderDetail(flag_1);
        }
        y.setOrderDetails(box);
//        y.setUser(user); //Dang bi loi
        // Save the Order entity
        return orderRepository.save(y);
    }

    @Transactional
    public Optional<Order> updateOrder(Integer id, OrderDtos request) {
        return orderRepository.findById(id).map(order -> {
            order.setAddress(request.getAddress());
            order.setCompanyName(request.getCompanyName());
            order.setPhoneNumber(request.getPhoneNumber());
            order.setEmailAddress(request.getEmailAddress());
//            order.setOrderStatus(request.getOrderStatus());
//            var a = orderDetailRepository.findByIdentification_order(id);
//            var for_save = updateOrderDetail(request.getOrderDetails(),a);
//            order.setOrderDetails(for_save);
            return orderRepository.save(order);
        });
    }

    public boolean deleteOrderAndOrderDetail(Integer id) {
        return orderRepository.findById(id).map(order -> {
            orderRepository.delete(order);
            orderDetailRepository.deleteByIdentificationOrder(order.getId());
            return true;
        }).orElse(false);
    }
//--------------------------OrderDetail----------------------------------

    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(order -> order.getOrderDetails())
                .orElse(null);
    }
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

//--------------------------OrderDetail(chưa dùng)----------------------------------
//        public List<OrderDetail> getAllOrderDetails() {
//        return orderDetailRepository.findAll();
//    }

//    public Optional<OrderDetail> getOrderDetailById(Integer id) {
//        return orderDetailRepository.findById(id);
//    }
    @Transactional
    public List<OrderDetail> updateOrderDetail(List<ItemDetailDto> request, List<OrderDetail> rightnow) {
        List<OrderDetail> for_save = new ArrayList<>();
        for (ItemDetailDto y : request ){
            for (OrderDetail x : rightnow){
                if(x.getId() == y.getId()){
                    x.setProduct_id(y.getProduct_id());
                    x.setQuantity(y.getQuantity());
                    x.setColor(y.getColors());
                    x.setSize(y.getSize());
                    for_save.add(x);
                    orderDetailRepository.save(x);
                }
            }
        }
        return for_save;
    }

    public boolean deleteOrderDetail(Integer id) {
        return orderDetailRepository.findById(id).map(orderDetail -> {
            orderDetailRepository.delete(orderDetail);
            return true;
        }).orElse(false);
    }
}
