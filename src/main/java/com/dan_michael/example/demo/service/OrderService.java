package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.OrderDtos;
import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.model.entities.SubEn.OrderDetail;
import com.dan_michael.example.demo.model.entities.SubEn.DetailSizeQuantity;
import com.dan_michael.example.demo.repositories.SupRe.OrderDetailRepository;
import com.dan_michael.example.demo.repositories.OrderRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
import com.dan_michael.example.demo.repositories.SupRe.DetailSizeQuantityRepository;
import com.dan_michael.example.demo.repositories.SupRe.QuantityDetailRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.dan_michael.example.demo.model.dto.ob.ItemDetailDto;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final QuantityDetailRepository quantityDetailRepository;

    private final DetailSizeQuantityRepository detailSizeQuantityRepository;

    private final ProductRepository productRepository;

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
        var totalQuantity = 0;
        var totalAmountOrder = 0.0F;
        var subAmountOrder = 0.0F;
        var user = userRepository.findById_create(request.getUserId());
        Order order = new Order();

        order.setIdentification_user(user.getId());

        order.setAddress(request.getAddress());
        order.setCompanyName(request.getCompanyName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setEmailAddress(request.getEmailAddress());

        order.setPaymentMethods(request.getPaymentMethods());
        order.setPaymentStatus(request.getPaymentStatus());

        order.setOrderStatus(request.getOrderStatus());

        order.setShippingStatus("ĐANG CHUẨN BỊ HÀNG");
        order.setCreatedAt(new Date());

        var y = orderRepository.save(order);
        List<OrderDetail> box = new ArrayList<>();
        for (ItemDetailDto x : request.getOrderDetails()) {
            OrderDetail detail = new OrderDetail();
            detail.setProduct_id(x.getProduct_id());

            var product = productRepository.findByID_(x.getProduct_id());
            var quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(product.getName());
            List<SubColor> BoxResponse = new ArrayList<>();
            for (var x_0: quantityDetailsList) {
                List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x_0.getColor());
                List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
                for (var y_0: detailSizeQuantities){
                    if(y_0.getIdentification().equals(x.getColor())){
                        if((y_0.getQuantity() > x.getQuantity()) && Objects.equals(x.getSize(), y_0.getSize())){
                            sizeQuantities.add(SubSizeQuantity.builder()
                                    .size(y_0.getSize())
                                    .quantity(y_0.getQuantity())
                                    .build());
                            y_0.setQuantity(y_0.getQuantity()-x.getQuantity());
                            detailSizeQuantityRepository.save(y_0);
                        }
                    }
                }
                BoxResponse.add(SubColor.builder()
                        .color(x_0.getColor())
                        .sizes(sizeQuantities)
                        .build());
                x_0.setSizeQuantities(detailSizeQuantities);
            }
            product.setQuantityDetails(quantityDetailsList);
            product.setTotalQuantity(totalQuantity);
            productRepository.save(product);

            detail.setQuantity(x.getQuantity());
            detail.setColor(x.getColor());
            detail.setSize(x.getSize());

            detail.setIdentification_order(y.getId());

            detail.setSubTotal(x.getSubTotal());

            var subdetail = x.getSubTotal() ;
            subAmountOrder += subdetail;
            box.add(detail);
            createOrderDetail(detail);
        }
        y.setOrderDetails(box);
        y.setShippingFee(request.getShippingFee());
        y.setTaxFee(request.getTaxFee());
        y.setSubTotal(subAmountOrder);
        totalAmountOrder = subAmountOrder + request.getShippingFee() + request.getTaxFee();
        y.setTotalAmountOrder(totalAmountOrder);
        return orderRepository.save(y);
    }

    @Transactional
    public Optional<Order> updateOrder(Integer id, OrderDtos request) {
        return orderRepository.findById(id).map(order -> {
            order.setAddress(request.getAddress());
            order.setCompanyName(request.getCompanyName());
            order.setPhoneNumber(request.getPhoneNumber());
            order.setEmailAddress(request.getEmailAddress());
            order.setOrderStatus(request.getOrderStatus());
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
        return orderDetailRepository.findByIdentification_order(orderId);
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
//    @Transactional
//    public List<OrderDetail> updateOrderDetail(List<ItemDetailDto> request, List<OrderDetail> rightnow) {
//        List<OrderDetail> for_save = new ArrayList<>();
//        for (ItemDetailDto y : request ){
//            for (OrderDetail x : rightnow){
//                if(x.getId() == y.getId()){
//                    x.setProduct_id(y.getProduct_id());
//                    x.setQuantity(y.getQuantity());
//                    x.setColor(y.getColor());
//                    x.setSize(y.getSize());
//                    for_save.add(x);
//                    orderDetailRepository.save(x);
//                }
//            }
//        }
//        return for_save;
//    }

    public boolean deleteOrderDetail(Integer id) {
        return orderDetailRepository.findById(id).map(orderDetail -> {
            orderDetailRepository.delete(orderDetail);
            return true;
        }).orElse(false);
    }
}
