package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.chatbot.entities.OriginalQuestion;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.resository.OriginalQuestionRepository;
import com.dan_michael.example.demo.chatbot.resository.QuestionAnswerRepository;
import com.dan_michael.example.demo.model.dto.ob.CommentDto;
import com.dan_michael.example.demo.model.dto.ob.OrderDtos;
import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import com.dan_michael.example.demo.model.entities.*;
import com.dan_michael.example.demo.model.entities.SubEn.OrderDetail;
import com.dan_michael.example.demo.model.entities.SubEn.DetailSizeQuantity;
import com.dan_michael.example.demo.model.response.OrderResponse;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.response.SubCart_OrderResponse;
import com.dan_michael.example.demo.repositories.*;
import com.dan_michael.example.demo.repositories.SupRe.OrderDetailRepository;
import com.dan_michael.example.demo.repositories.SupRe.DetailSizeQuantityRepository;
import com.dan_michael.example.demo.repositories.SupRe.QuantityDetailRepository;
import com.dan_michael.example.demo.repositories.image.ProductImgRepository;
import com.dan_michael.example.demo.util.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import com.dan_michael.example.demo.model.dto.ob.ItemDetailDto;

import java.util.*;

import static com.dan_michael.example.demo.chatbot.service.ChatbotService.removeDiacritics;
import static com.dan_michael.example.demo.util.UtilFunction.saveInfoChatBotAnswer;
import static com.dan_michael.example.demo.util.UtilFunction.saveQuantityDetailAnswer;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final QuantityDetailRepository quantityDetailRepository;

    private final DetailSizeQuantityRepository detailSizeQuantityRepository;

    private final ProductRepository productRepository;

    private final ProductService productService;

    private final DiscountRepository discountRepository;

    private final QuestionAnswerRepository questionAnswerRepository;

    private final OriginalQuestionRepository originalQuestionRepository;
//--------------------------Order----------------------------------
    public List<Order> getAllOrders() {
        var flag = orderRepository.findByAllOrderActive();
        flag.removeIf(x -> Objects.equals(x.getOrderStatus(), Constants.Order_Status_Cancelled));
        if(flag == null){
            return null;
        }
        flag.forEach(x->x.setOrderDetails(orderDetailRepository.findByIdentification_order(x.getId())));
        return flag;
    }

//    public List<Order> getAllOrdersPaymentStatus() {
//        var flag = orderRepository.findByAllOrderActive();
//        flag.forEach(x->x.setOrderDetails(orderDetailRepository.findByIdentification_order(x.getId())));
//        return flag;
//    }

    public List<Order> getAllOrdersByOrderStatus(String orderStatus) {
        var flag = orderRepository.findByAllOrderByAdmin_OrderStatus(orderStatus);
        flag.forEach(x->x.setOrderDetails(orderDetailRepository.findByIdentification_order(x.getId())));
        return flag;
    }

    public Optional<Order> getOrderById(Integer id) {
        var flag = orderRepository.findById(id);
        flag.get().setOrderDetails(orderDetailRepository.findByIdentification_order(id));
        return flag;
    }
    public List<Order> getOrderByUserId(Integer id) {
        var flag = orderRepository.findByAllOrderByUser(id);
        for(var x:flag){
            x.setOrderDetails(orderDetailRepository.findByIdentification_user(id,x.getId()));
        }
        return flag;
    }

    public List<Order> getOrderByUserId_PaymentStatus(Integer id, Integer paymentStatus) {
        var flag = orderRepository.findByAllOrderByUser_PaymentStatus(id,paymentStatus);
        for(var x:flag){
            x.setOrderDetails(orderDetailRepository.findByIdentification_user(id,x.getId()));
        }
        return flag;
    }
    public List<Order> getOrderByUserId_OrderStatus(Integer id, String orderStatus) {
        var flag = orderRepository.findByAllOrderByUser_OrderStatus(id,orderStatus);
        for(var x:flag){
            x.setOrderDetails(orderDetailRepository.findByIdentification_user(id,x.getId()));
        }
        return flag;
    }

    public List<Order> getOrderByUserId_Both(Integer id,Integer paymentStatus ,String orderStatus) {
        var flag = orderRepository.findByAllOrderByUser_Both(id,paymentStatus,orderStatus);
        for(var x:flag){
            x.setOrderDetails(orderDetailRepository.findByIdentification_user(id,x.getId()));
        }
        return flag;
    }
//    public List<String> getsize(){
//
//    }
    @Transactional
    public OrderResponse createOrder(OrderDtos request) {
        var subtotalProduct = 0;
        var totalQuantity_order = 0;
        var totalAmountOrder = 0.0f;
        var subAmountOrder = 0.0f;
        var user = userRepository.findById_create(request.getUserId());
        var discount = discountRepository.findBySku(request.getSkuDiscount());
        Order order = new Order();
        order.setSkuOrder(ProductService.generateSku());
        order.setIdentification_user(user.getId());
        order.setBuyer_name(user.getName());
        order.setAddress(request.getAddress());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setEmailAddress(request.getEmailAddress());
        order.setPaymentMethods(request.getPaymentMethods());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setCreatedAt(new Date());

        List<SubCart_OrderResponse> boxItem = new ArrayList<>();
        var y = orderRepository.save(order);
        List<OrderDetail> box = new ArrayList<>();
        for (ItemDetailDto x : request.getOrderDetails()) {
            OrderDetail detail = new OrderDetail();
            detail.setItemDetail_id(x.getItemDetail_id());
            var image_Detail = productRepository.findByName(x.getName()).get().getImageMain();
            var product = productRepository.findByID_(x.getItemDetail_id());
            var quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(product.getName());
            List<String> sizes = new ArrayList<>();
            List<String> colors = new ArrayList<>();
            List<SubColor> BoxResponse = new ArrayList<>();
            for (var x_0: quantityDetailsList) {
                List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x_0.getColor(),x_0.getIdentification());
                if(!colors.contains(x_0.getColor())){
                    colors.add(x_0.getColor());
                }
                List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
                for (var y_0: detailSizeQuantities){
                    if(!sizes.contains(y_0.getSize())){
                        sizes.add(y_0.getSize());
                    }
                    if(y_0.getIdentification().equals(x.getColor())){
                        if((y_0.getQuantity() >= x.getQuantity()) && Objects.equals(x.getSize(), y_0.getSize())){
                            sizeQuantities.add(SubSizeQuantity.builder()
                                    .size(y_0.getSize())
                                    .quantity(y_0.getQuantity()-x.getQuantity())
                                    .build());
                            y_0.setQuantity(y_0.getQuantity()-x.getQuantity());
                            detailSizeQuantityRepository.save(y_0);
                            subtotalProduct += x.getQuantity();
                            product.setQuantitySold(product.getQuantitySold()+x.getQuantity());
                            CommentDto commentDto = CommentDto
                                    .builder()
                                    .rating(0.0f)
                                    .idOrder(y.getId())
                                    .username(userRepository.findById(request.getUserId()).get().getName())
                                    .color(x.getColor())
                                    .size(y_0.getSize())
                                    .build();
                            try {
                                    commentDto.setStatus(0);
                                    productService.createComment(commentDto,x.getItemDetail_id());
//                                }
                            } catch (ChangeSetPersister.NotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                BoxResponse.add(SubColor.builder()
                        .color(x_0.getColor())
                        .sizes(sizeQuantities)
                        .build());
                x_0.setSizeQuantities(detailSizeQuantities);
            }
//            update question
            QuestionAnswer qa0 = questionAnswerRepository.findByQuestion(product.getSkuQa().toLowerCase());
            qa0.setAnswer(saveInfoChatBotAnswer(product,sizes,colors,BoxResponse));

            var oriqe10 = ("Số lượng chi tiết của "+ product.getName());
            QuestionAnswer qa10 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe10.toLowerCase()));
            qa10.setAnswer(saveQuantityDetailAnswer(product.getName(),sizes,colors,BoxResponse));

            totalQuantity_order += x.getQuantity();
            // cập nhập quantity product
            product.setQuantityDetails(quantityDetailsList);
            product.setTotalQuantity(product.getTotalQuantity()-subtotalProduct);
            productRepository.save(product);
            var oriqe4 = ("Sản phẩm "+ product.getName()+" có còn hàng không?");
            QuestionAnswer qa4 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe4.toLowerCase()));
            qa4.setAnswer("Tổng hàng tồn kho của "+ product.getName()+" còn "+product.getTotalQuantity()+" sản phẩm các loại, Để kiểm tra tình trạng hàng tồn kho chi tiết, Bạn vui lòng nhấn vào trang sản phẩm của chúng tôi. ");

            detail.setName(x.getName());
            detail.setQuantity(x.getQuantity());
            detail.setColor(x.getColor());
            detail.setSize(x.getSize());
            detail.setImage(image_Detail);
            detail.setIdentification_order(y.getId());
            detail.setIdentification_user(user.getId());
            detail.setUnitPrice(x.getUnitPrice());
            detail.setTotalPrice(x.getTotalPrice());
            var itemCart = SubCart_OrderResponse.builder()
                    .itemDetail_id(y.getId())
                    .name(x.getName())
                    .size(x.getSize())
                    .image(image_Detail)
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
        // luu giao dich

        y.setOrderDetails(box);
        y.setShippingFee(request.getShippingFee());
        y.setTaxFee(0.0f);
        y.setPercentDiscount(request.getPercentDiscount());
        if(request.getPercentDiscount() > 0 && discount != null){
            totalAmountOrder = (totalAmountOrder + request.getShippingFee());
            totalAmountOrder = totalAmountOrder - totalAmountOrder*((float) request.getPercentDiscount() /100);
            discount.setStatus(0);
            discountRepository.save(discount);
        }else {
            totalAmountOrder = totalAmountOrder + request.getShippingFee();
        }
        y.setTotalAmountOrder(totalAmountOrder);
        orderRepository.save(y);
        List<Product> box0 = productRepository.search_all(null,null,null,null, null, null, null, null, null, null, null);
        List<Product> box1 = productRepository.search_all(null,null,null,null, null, true, null, null, null, null, null);
        List<Product> box2 = productRepository.search_all(null,null,null,null, null, null, true, null, null, null, null);
        box0.sort(Comparator.comparing(Product::getQuantitySold).reversed());
        var oriqe23 = ("Gợi ý cho tôi các sản phẩm bán chạy nhất");
        var oriqe24 = ("Gợi ý cho tôi các sản phẩm giảm giá ổn nhất");
        var oriqe25 = ("Gợi ý cho tôi các sản phẩm mới nhất");
        QuestionAnswer qa23 = new QuestionAnswer();
        QuestionAnswer qa24 = new QuestionAnswer();
        QuestionAnswer qa25 = new QuestionAnswer();
        if(questionAnswerRepository.findByQuestion(removeDiacritics(oriqe23.toLowerCase())) == null &&
                questionAnswerRepository.findByQuestion(removeDiacritics(oriqe24.toLowerCase())) == null &&
                questionAnswerRepository.findByQuestion(removeDiacritics(oriqe25.toLowerCase())) == null
        ){
                    originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe23).build());
                    originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe24).build());
                    originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe25).build());
            qa23 = QuestionAnswer.builder()
                    .question(removeDiacritics(oriqe23.toLowerCase()))
                    .answer("Dưới đây là danh sách các sản phẩm bán chạy nhất hiện nay mà bạn có thể tham khảo: \\n1. "+box0.get(0).getName()+" - Đánh giá cao, nhiều người dùng yêu thích. \\n2. "+box0.get(1).getName()+" - Đang được rất nhiều khách hàng lựa chọn. \\n3. "+box0.get(2).getName()+" - Nhận được phản hồi tích cực từ khách hàng gần đây. Hãy thử xem những sản phẩm này có phù hợp với bạn không nhé!\"")
                    .build();
            qa24 = QuestionAnswer.builder()
                    .question(removeDiacritics(oriqe24.toLowerCase()))
                    .answer("Đây là các sản phẩm đang được giảm giá tốt nhất mà tôi tìm thấy cho bạn: \n1. "+box1.get(0).getName()+" - Giảm giá mạnh với ưu đãi đặc biệt. \n2. "+box1.get(1).getName()+" - Khuyến mãi lớn, chất lượng đáng tin cậy. \n3. "+box1.get(2).getName()+" - Đang có mức giá tốt nhất so với các sản phẩm tương tự.")
                    .build();
            qa25 = QuestionAnswer.builder()
                    .question(removeDiacritics(oriqe25.toLowerCase()))
                    .answer("Dưới đây là một số sản phẩm mới nhất mà bạn có thể muốn xem qua: \\n1. "+box2.get(0).getName()+" - Sản phẩm mới ra mắt, được nhiều người quan tâm. \\n2. "+box2.get(1).getName()+" - Thiết kế hiện đại, tính năng độc đáo. \\n3. "+box2.get(2).getName()+" - Một trong những sản phẩm mới hứa hẹn mang lại trải nghiệm tuyệt vời cho người dùng.")
                    .build();
            questionAnswerRepository.save(qa23);
            questionAnswerRepository.save(qa24);
            questionAnswerRepository.save(qa25);
        }else {
            qa23 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe23.toLowerCase()));
            qa23.setAnswer("Dưới đây là danh sách các sản phẩm bán chạy nhất hiện nay mà bạn có thể tham khảo: \\n1. "+box0.get(0).getName()+" - Đánh giá cao, nhiều người dùng yêu thích. \\n2. "+box0.get(1).getName()+" - Đang được rất nhiều khách hàng lựa chọn. \\n3. "+box0.get(2).getName()+" - Nhận được phản hồi tích cực từ khách hàng gần đây. Hãy thử xem những sản phẩm này có phù hợp với bạn không nhé!\"");
            qa24 = questionAnswerRepository.findByQuestion(removeDiacritics(removeDiacritics(oriqe24.toLowerCase())));
            qa23.setAnswer("Đây là các sản phẩm đang được giảm giá tốt nhất mà tôi tìm thấy cho bạn: \n1. "+box1.get(0).getName()+" - Giảm giá mạnh với ưu đãi đặc biệt. \n2. "+box1.get(1).getName()+" - Khuyến mãi lớn, chất lượng đáng tin cậy. \n3. "+box1.get(2).getName()+" - Đang có mức giá tốt nhất so với các sản phẩm tương tự.");
            qa25 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe25.toLowerCase()));
            qa23.setAnswer("Dưới đây là một số sản phẩm mới nhất mà bạn có thể muốn xem qua: \\n1. "+box2.get(0).getName()+" - Sản phẩm mới ra mắt, được nhiều người quan tâm. \\n2. "+box2.get(1).getName()+" - Thiết kế hiện đại, tính năng độc đáo. \\n3. "+box2.get(2).getName()+" - Một trong những sản phẩm mới hứa hẹn mang lại trải nghiệm tuyệt vời cho người dùng.");
        }
        questionAnswerRepository.save(qa23);
        questionAnswerRepository.save(qa24);
        questionAnswerRepository.save(qa25);
        return OrderResponse.builder()
                .order_id(y.getId())
                .skuOrder(y.getSkuOrder())
                .user_id(y.getIdentification_user())
                .orderDetails(boxItem)
                .address(y.getAddress())
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
    public Optional<Order> updateOrderAdmin(Integer id, OrderDtos request) {
        return orderRepository.findById(id).map(order -> {
            if((order.getPaymentStatus() == 0 && request.getPaymentStatus() == 1 )|| order.getPaymentStatus() == 1){
                var comment = commentRepository.findCommentStatus0(id);
                System.out.println(comment.size());
                for(var change :comment){
                    if(Objects.equals(request.getOrderStatus(), Constants.Order_Status_Received)){
                        change.setStatusActive(1);
                        commentRepository.save(change);
                    }
                }
            }
            order.setPaymentStatus(request.getPaymentStatus());
            order.setOrderStatus(request.getOrderStatus());
            return orderRepository.save(order);
        });
    }

    @Transactional
    public Optional<Order> CloneTime(Integer id, OrderDtos request) {
        return orderRepository.findById(id).map(order -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(request.getYear(), request.getMonth()-1, request.getDay());  // Năm, Tháng (0-11), Ngày
            Date fixedDate = calendar.getTime();
            order.setCreatedAt(fixedDate);
            return orderRepository.save(order);
        });
    }

    @Transactional
    public Optional<Order> updateOrderUser(Integer id, OrderDtos request) {
        return orderRepository.findById(id).map(order -> {
            order.setAddress(request.getAddress());
            order.setPhoneNumber(request.getPhoneNumber());
            order.setEmailAddress(request.getEmailAddress());
            return orderRepository.save(order);
        });
    }

    @Transactional
    public ResponseMessageDtos CanceledOrder(Integer id) {
        return orderRepository.findById(id).map(order -> {
            if (order.getOrderStatus() == null ||
                    (order.getOrderStatus() == Constants.Order_Status_Received && order.getPaymentStatus() == 1)) {
                order.setOrderStatus(Constants.Order_Status_Cancelled);
                orderRepository.save(order);
                return ResponseMessageDtos.builder()
                        .status(200)
                        .message("Order cancelled successfully.")
                        .build();
            }
            else {
                return ResponseMessageDtos.builder()
                        .status(400)
                        .message("Order cannot be cancelled.")
                        .build();
            }
        }).orElseGet(() -> ResponseMessageDtos.builder()
                .status(404)
                .message("Order not found.")
                .build());
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
