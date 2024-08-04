package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.OrderDtos;
import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.model.entities.SubEn.OrderDetail;
import com.dan_michael.example.demo.model.entities.SubEn.DetailSizeQuantity;
import com.dan_michael.example.demo.model.response.OrderResponse;
import com.dan_michael.example.demo.model.response.SubCart_OrderResponse;
import com.dan_michael.example.demo.repositories.SupRe.OrderDetailRepository;
import com.dan_michael.example.demo.repositories.OrderRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
import com.dan_michael.example.demo.repositories.SupRe.DetailSizeQuantityRepository;
import com.dan_michael.example.demo.repositories.SupRe.QuantityDetailRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import com.dan_michael.example.demo.util.Constants;
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
    public OrderResponse createOrder(OrderDtos request) {
        var subtotalProduct = 0;
        var totalQuantity_order = 0;
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
        order.setShippingStatus(Constants.Order_Status_Wait);
        order.setCreatedAt(new Date());

        List<SubCart_OrderResponse> boxItem = new ArrayList<>();
        var y = orderRepository.save(order);
        List<OrderDetail> box = new ArrayList<>();
        for (ItemDetailDto x : request.getOrderDetails()) {
            OrderDetail detail = new OrderDetail();
            detail.setProduct_id(x.getItemDetail_id());

            var product = productRepository.findByID_(x.getItemDetail_id());
            var quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(product.getName());
            List<SubColor> BoxResponse = new ArrayList<>();
            for (var x_0: quantityDetailsList) {
                List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x_0.getColor(),x_0.getIdentification());
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
                            subtotalProduct += x.getQuantity();
                        }
                    }
                }
                BoxResponse.add(SubColor.builder()
                        .color(x_0.getColor())
                        .sizes(sizeQuantities)
                        .build());
                x_0.setSizeQuantities(detailSizeQuantities);
            }
            totalQuantity_order += x.getQuantity();
            // cập nhập quantity product
            product.setQuantityDetails(quantityDetailsList);
            product.setTotalQuantity(product.getTotalQuantity()-subtotalProduct);
            productRepository.save(product);

            detail.setQuantity(x.getQuantity());
            detail.setColor(x.getColor());
            detail.setSize(x.getSize());
            detail.setIdentification_order(y.getId());
            detail.setUnitPrice(x.getUnitPrice());
            detail.setTotalPrice(x.getTotalPrice());
            var itemCart = SubCart_OrderResponse.builder()
                    .itemDetail_id(y.getId())
                    .name(x.getName())
                    .size(x.getSize())
                    .image(x.getImage())
                    .color(x.getColor())
                    .quantity(x.getQuantity())
                    .unitPrice(x.getUnitPrice())
                    .totalPrice(x.getTotalPrice())
                    .build();
            boxItem.add(itemCart);
            subAmountOrder += x.getTotalPrice();
            totalAmountOrder += x.getTotalPrice();
            box.add(detail);
            createOrderDetail(detail);
        }
        y.setOrderDetails(box);
        y.setShippingFee(request.getShippingFee());
        y.setTaxFee(request.getTaxFee());
        y.setPercentDiscount(request.getPercentDiscount());
        if(request.getPercentDiscount() > 0){
            totalAmountOrder = (totalAmountOrder + request.getShippingFee() + request.getTaxFee());
            totalAmountOrder = totalAmountOrder - totalAmountOrder*(request.getPercentDiscount()/100);
        }else {
            totalAmountOrder = totalAmountOrder + request.getShippingFee() + request.getTaxFee();
        }
        y.setTotalAmountOrder(totalAmountOrder);
        orderRepository.save(y);
        return OrderResponse.builder()
                .order_id(y.getId())
                .user_id(y.getIdentification_user())
                .orderDetails(boxItem)
                .address(y.getAddress())
                .companyName(y.getCompanyName())
                .phoneNumber(y.getPhoneNumber())
                .emailAddress(y.getEmailAddress())
                .paymentMethods(y.getPaymentMethods())
                .paymentStatus(y.getPaymentStatus())
                .unitPrice(subAmountOrder)
                .shippingFee(y.getShippingFee())
                .taxFee(y.getTaxFee())
                .percentDiscount(y.getPercentDiscount())
                .totalPayment(totalAmountOrder)
                .totalQuantity(totalQuantity_order)
                .orderStatus(y.getOrderStatus())
                .build();
    }

    @Transactional
    public Optional<Order> updateOrder(Integer id, OrderDtos request) {
        return orderRepository.findById(id).map(order -> {
            order.setAddress(request.getAddress());
            order.setCompanyName(request.getCompanyName());
            order.setPhoneNumber(request.getPhoneNumber());
            order.setEmailAddress(request.getEmailAddress());
            order.setOrderStatus(request.getOrderStatus());
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

    public boolean deleteOrderDetail(Integer id) {
        return orderDetailRepository.findById(id).map(orderDetail -> {
            orderDetailRepository.delete(orderDetail);
            return true;
        }).orElse(false);
    }
}
