package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.chatbot.entities.OriginalQuestion;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.resository.OriginalQuestionRepository;
import com.dan_michael.example.demo.chatbot.resository.QuestionAnswerRepository;
import com.dan_michael.example.demo.chatbot.resository.QuestionOfGuestRepository;
import com.dan_michael.example.demo.model.dto.global.PaginationDto;
import com.dan_michael.example.demo.model.dto.ob.*;
import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import com.dan_michael.example.demo.model.entities.*;
import com.dan_michael.example.demo.model.response.*;
import com.dan_michael.example.demo.model.entities.SubEn.*;
import com.dan_michael.example.demo.model.entities.img.ProductImg;
import com.dan_michael.example.demo.model.response.barChartData.ChartData;
import com.dan_michael.example.demo.model.response.barChartData.Data;
import com.dan_michael.example.demo.repositories.*;
import com.dan_michael.example.demo.repositories.image.ProductImgRepository;
import com.dan_michael.example.demo.repositories.SupRe.*;
import com.dan_michael.example.demo.util.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;

import static com.dan_michael.example.demo.chatbot.service.ChatbotService.removeDiacritics;
import static com.dan_michael.example.demo.util.UtilFunction.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final CommentRepository commentRepository;

    private final ProductImgRepository productImgRepository;

    private final QuantityDetailRepository quantityDetailRepository;

    private final DetailSizeQuantityRepository detailSizeQuantityRepository;

    private final TradeMarkRepository tradeMarkRepository;

    private final FavouriteProductRepository favouriteProductRepository;

    private final QuestionAnswerRepository questionAnswerRepository;

    private final QuestionOfGuestRepository questionOfGuestRepository;

    private final OriginalQuestionRepository originalQuestionRepository;

    private final StyleRepository styleRepository;

    private final CategoryRepository categoryRepository;

    private final MaterialRepository materialRepository;

    private final QRInfoRepository qrInfoRepository;

//-------------------Product-----------------------------------
    public static String generateSku() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase().substring(0, 10);
    }
    public List<ProductChatBotResponse> findProduct_ChatBot() {
        List<ProductChatBotResponse> save = new ArrayList<>();
        var pros = productRepository.findAll();
        pros.sort(Comparator.comparing(Product::getFinalPrice).reversed());
        for (var x : pros) {
            var y = ProductChatBotResponse.builder()
                    .sku(x.getSkuQa())
                    .name(x.getName())
                    .imageMain(x.getImageMain())
                    .originalPrice(x.getOriginalPrice())
                    .finalPrice(x.getFinalPrice())
                    .build();
            save.add(y);
        }
        return save;
    }

//-------------------Product-----------------------------------
    public ProductResponse createProduct(ProductDtos request) {
        var totalQuantity = 0;
        List<String> sizes = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        var ob = productRepository.findByName(request.getName());
        if(ob.isPresent()){
            return null;
        }
        var product_flag = new Product();
        String sku = generateSku();
        product_flag.setSkuQa(sku);
        product_flag.setName(request.getName());
        product_flag.setDescription(request.getDescription());

        if(request.getQuantitySold() != null){
            product_flag.setQuantitySold(request.getQuantitySold());
        }else{
            product_flag.setQuantitySold(0);
        }
        product_flag.setStyle(request.getStyle());
        product_flag.setMaterial(request.getMaterial());
        product_flag.setCategory(request.getCategory());
        product_flag.setSubCategory(request.getSubCategory());
        product_flag.setTradeMask(request.getTradeMask());
        product_flag.setRating(5.0f);
        product_flag.setNRating(0);
        var finalPrice = 0.0f;
        if(request.getSaleDiscountPercent() > 0){
            product_flag.setSaleStatus(true);
            product_flag.setOriginalPrice(request.getOriginalPrice());
            product_flag.setSaleDiscountPercent(request.getSaleDiscountPercent());
            finalPrice = request.getOriginalPrice() - request.getOriginalPrice()* (request.getSaleDiscountPercent()/100);
        }else{
            product_flag.setSaleStatus(false);
            finalPrice = request.getOriginalPrice();
            product_flag.setOriginalPrice(request.getOriginalPrice());
            product_flag.setSaleDiscountPercent(0.0f);
        }
        product_flag.setFinalPrice(finalPrice);
        product_flag.setNewStatus(true);
        if(product_flag.getQuantitySold() !=null && product_flag.getQuantitySold() >16){
            product_flag.setNewStatus(false);
        }
        product_flag.setFavourite(null);

        product_flag.setCreateDate(new Date());
        product_flag.setCreatedByUserid(request.getCreatedByUserid());

        List<QuantityDetail> Box = new ArrayList<>();
        List<SubColor> BoxResponse = new ArrayList<>();

        if (request.getQuantityDetails() != null && !request.getQuantityDetails().isEmpty()) {
            for (var x : request.getQuantityDetails()) {
                QuantityDetail Item = new QuantityDetail();
                List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
                Item.setColor(x.getColor());
                if(!colors.contains(x.getColor())){
                    colors.add(x.getColor());
                }
                for (var y : x.getSizes()){
                    DetailSizeQuantity detailSizeQuantity = new DetailSizeQuantity();
                    detailSizeQuantity.setSize(y.getSize());
                    detailSizeQuantity.setQuantity(y.getQuantity());
                    detailSizeQuantity.setIdentification(x.getColor());
                    detailSizeQuantity.setIdentification_pro(product_flag.getName());
                    detailSizeQuantityRepository.save(detailSizeQuantity);
                    totalQuantity += y.getQuantity();
                    if(!sizes.contains(y.getSize())){
                        sizes.add(y.getSize());
                    }
                    sizeQuantities.add(SubSizeQuantity.builder()
                            .size(y.getSize())
                            .quantity(y.getQuantity())
                            .build());
                }
                Item.setIdentification(product_flag.getName());
                Box.add(Item);
                BoxResponse.add(SubColor.builder()
                        .color(x.getColor())
                        .sizes(sizeQuantities)
                        .build());
                quantityDetailRepository.save(Item);
            }
        }


//        List<SubImgResponse> productImagesBox = new ArrayList<>();
        List<String> productImagesBox = new ArrayList<>();
        List<ProductImg> productImagesBox_0 = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (MultipartFile imageFile : request.getImages()) {
                ProductImg productImg = new ProductImg();
                try {
                    productImg.setImage(imageFile.getBytes()); // Save image bytes
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle exception
                }
                productImg.setIdentification(product_flag.getName()); // Set the product reference
                productImg.setImageName(imageFile.getOriginalFilename());
                productImg.setImg_url(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(Constants.Global_Image_Path)
                        .path(product_flag.getName()+"/")
                        .path(imageFile.getOriginalFilename())
                        .toUriString());
                productImgRepository.save(productImg);
                if(Objects.equals(productImg.getImageName(), request.getImageMain())){
                    product_flag.setImageMain(productImg.getImg_url());
                }
                productImagesBox.add(productImg.getImg_url());
                productImagesBox_0.add(productImg);
            }
        }

        product_flag.setQuantityDetails(Box);
        product_flag.setTotalQuantity(totalQuantity);
        product_flag.setImages(productImagesBox_0);
        if(request.getImageMain().contains("https://product.hstatic.net")){
            product_flag.setImageMain(request.getImageMain());
        }
        productRepository.save(product_flag);
        var save =  ProductResponse.builder()
                .id(product_flag.getId())
                .images(productImagesBox)
                .colours(colors)
                .sizes(sizes)
                .quantitySold(product_flag.getQuantitySold())
                .style(product_flag.getStyle())
                .material(product_flag.getMaterial())
                .tradeMask(request.getTradeMask())
                .subCategory(request.getSubCategory())
                .name(product_flag.getName())
                .description(product_flag.getDescription())
                .quantityDetails(BoxResponse)
                .category(product_flag.getCategory())
                .rating(product_flag.getRating())
                .nRating(product_flag.getNRating())
                .totalQuantity(totalQuantity)
                .favourite(null)
                .imageMain(product_flag.getImageMain())
                .originalPrice(product_flag.getOriginalPrice())
                .saleDiscountPercent(product_flag.getSaleDiscountPercent())
                .finalPrice(product_flag.getFinalPrice())
                .saleStatus(product_flag.getSaleStatus())
                .newStatus(product_flag.getNewStatus())
                .comments(product_flag.getComments())
                .createDate(product_flag.getCreateDate())
                .createdByUserid(product_flag.getCreatedByUserid())
                .build();
        //originalQuestion
//        var oriqe0 = product_flag.getName();
        var oriqe4 = ("Sản phẩm "+ save.getName()+" có còn hàng không?");
        var oriqe3 = ("Hiện tại "+ save.getName()+" có chương trình giảm giá hoặc khuyến mãi nào không?");
        var oriqe5 = ("Tôi có thể xem đánh giá của khách hàng về "+ save.getName()+" ở đâu?");
        var oriqe6 = ("Tôi có thể trả lại "+ save.getName()+" nếu không hài lòng không?");
        var oriqe8 = ("Màu sắc của "+ save.getName()+" có những lựa chọn nào ?");
        var oriqe9 = ("Kích cỡ của "+ save.getName()+" có những lựa chọn nào ?");
        var oriqe10 = ("Số lượng chi tiết của "+ save.getName());
        var oriqe13 = ("Giá gốc của "+ save.getName()+" là bao nhiêu?");
        var oriqe14 = ("Giá hiện tại của "+ save.getName()+" là bao nhiêu?");

        var oriqe15 = (save.getName()+" có chương trình tích điểm hoặc nhận quà khi mua hàng không?");
        var oriqe16 = (save.getName()+" có co giãn tốt không?");
        var oriqe17 = ("Chất liệu của " +save.getName()+" có bền màu sau nhiều lần giặt không?");
        var oriqe18 = ("Có gói quà tặng hoặc dịch vụ bọc quà khi mua " +save.getName()+" không?");
        var oriqe19 = ("Có phụ kiện hoặc trang phục nào đi kèm với " +save.getName()+" không?");
        var oriqe20 = ("Màu sắc của " +save.getName()+" có dễ phai sau nhiều lần giặt không?");
        var oriqe21 = ("Khi mặc, " +save.getName()+" có cảm giác thoải mái không?");
        var oriqe22 = ("Có phiên bản giới hạn (limited edition) của" +save.getName()+" không?");

//        var oriqe23 = ("Gợi ý cho tôi các sản phẩm bán chạy nhất");
//        var oriqe24 = ("Gợi ý cho tôi các sản phẩm giảm giá ổn nhất");
//        var oriqe25 = ("Gợi ý cho tôi các sản phẩm mới nhất");

//        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe0).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe4).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe3).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe5).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe6).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe8).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe9).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe10).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe13).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe14).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe15).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe16).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe17).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe18).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe19).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe20).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe21).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe22).build());

//        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe23).build());
//        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe24).build());
//        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe25).build());


        //info
        QuestionAnswer qa0 = QuestionAnswer.builder()
                .question(removeDiacritics(product_flag.getSkuQa().toLowerCase()))
                .answer(saveInfoChatBotAnswer(save,sizes,colors,BoxResponse))
                .build();
        //Sản phẩm [tên sản phẩm] có còn hàng không?
        QuestionAnswer qa4 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe4.toLowerCase()))
                .answer("Tổng hàng tồn kho của "+ save.getName()+" còn "+save.getTotalQuantity()+" sản phẩm các loại, Để kiểm tra tình trạng hàng tồn kho chi tiết, Bạn vui lòng nhấn vào trang sản phẩm của chúng tôi. ")
                .build();
        //Bạn có chương trình khuyến mãi nào cho [tên sản phẩm] không?
        QuestionAnswer qa3 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe3.toLowerCase()))
//                .answer("Hiện tại, chúng tôi đang có chương trình khuyến mãi "+ save.getSaleDiscountPercent()+"% cho "+ save.getName()+". Bạn có thể tìm hiểu thêm tại trang khuyến mãi của chúng tôi.")
                .build();
        if(save.getSaleDiscountPercent() !=0) {
            qa3.setAnswer("Hiện tại, chúng tôi đang có chương trình khuyến mãi "+ save.getSaleDiscountPercent()+"% cho "+ save.getName()+". Bạn có thể tìm hiểu thêm tại trang khuyến mãi của chúng tôi.");
        }else {
            qa3.setAnswer("Hiện tại, chúng tôi không có chương trình khuyến mãi cho "+ save.getName()+". Bạn có thể tìm hiểu thêm tại trang khuyến mãi của chúng tôi.");
        }
        //Tôi có thể xem đánh giá của khách hàng về [tên sản phẩm] ở đâu?
        QuestionAnswer qa5 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe5.toLowerCase()))
                .answer("Bạn có thể xem đánh giá của khách hàng về "+ save.getName()+" tại trang sản phẩm dưới phần đánh giá và nhận xét.")
                .build();
        //Tôi có thể trả lại [tên sản phẩm] nếu không hài lòng không?
        QuestionAnswer qa6 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe6.toLowerCase()))
                .answer("Vâng, bạn có thể trả lại "+ save.getName()+" trong vòng 2 ngày kể từ ngày nhận hàng, theo chính sách đổi trả của chúng tôi. Bạn vui lòng kiểm tra chi tiết trong phần chính sách đổi trả.")
                .build();
        // [tên sản phẩm] hiện Có màu sắc nào?
        QuestionAnswer qa8 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe8.toLowerCase()))
                .answer(savesizesAnswer(save.getName(),colors))
                .build();
        // [tên sản phẩm] hiện Có kích thước nào?
        QuestionAnswer qa9 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe9.toLowerCase()))
                .answer(savecolorsAnswer(save.getName(),sizes))
                .build();
        //Chi tiết Quantity Detail of "+ namepro
        QuestionAnswer qa10 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe10.toLowerCase()))
                .answer(saveQuantityDetailAnswer(save,sizes,colors,BoxResponse))
                .build();
        //Giá gốc của [tên sản phẩm] là bao nhiêu?
        QuestionAnswer qa13 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe13.toLowerCase()))
                .answer("Giá gốc của "+ save.getName()+" là "+save.getOriginalPrice() + " VND")
                .build();
        // Giá khuyến mãi hiện tại của [tên sản phẩm] là bao nhiêu?
        QuestionAnswer qa14 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe14.toLowerCase()))
                .answer("Giá khuyến mãi hiện tại của "+ save.getName()+" là "+save.getFinalPrice() + "VND")
                .build();
        QuestionAnswer qa15 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe15.toLowerCase()))
                .answer("Có, " + save.getName() + " có chương trình tích điểm và bạn sẽ nhận được quà tặng khi tích đủ điểm.")
                .build();
        QuestionAnswer qa16 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe16.toLowerCase()))
                .answer("Sản phẩm " + save.getName() + " có độ co giãn tốt, phù hợp với nhiều kích cỡ khác nhau.")
                .build();
        QuestionAnswer qa17 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe17.toLowerCase()))
                .answer("Chất liệu của " + save.getName() + " bền màu và không phai sau nhiều lần giặt.")
                .build();
        QuestionAnswer qa18 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe18.toLowerCase()))
                .answer("Có, chúng tôi cung cấp gói quà tặng khi bạn đặt hàng sản phẩm " + save.getName() + ".")
                .build();
        QuestionAnswer qa19 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe19.toLowerCase()))
                .answer("Sản phẩm " + save.getName() + " không đi kèm phụ kiện, nhưng bạn có thể mua riêng các phụ kiện phù hợp.")
                .build();
        QuestionAnswer qa20 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe20.toLowerCase()))
                .answer("Màu sắc của " + save.getName() + " không dễ phai sau nhiều lần giặt, bạn có thể yên tâm sử dụng.")                .build();
        QuestionAnswer qa21 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe21.toLowerCase()))
                .answer("Khi mặc, " + save.getName() + " rất thoải mái và không gây khó chịu cho người sử dụng.")
                .build();
        QuestionAnswer qa22 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe22.toLowerCase()))
                .answer("Hiện tại không có phiên bản giới hạn của " + save.getName() + ", nhưng chúng tôi sẽ thông báo nếu có. Bạn có thể để lại thắc mắc ở trong phần liên lạc của chúng tôi !")
                .build();

        questionAnswerRepository.save(qa0);
        questionAnswerRepository.save(qa3);
        questionAnswerRepository.save(qa4);
        questionAnswerRepository.save(qa5);
        questionAnswerRepository.save(qa6);
        questionAnswerRepository.save(qa8);
        questionAnswerRepository.save(qa9);
        questionAnswerRepository.save(qa10);
        questionAnswerRepository.save(qa13);
        questionAnswerRepository.save(qa14);
        questionAnswerRepository.save(qa15);
        questionAnswerRepository.save(qa16);
        questionAnswerRepository.save(qa17);
        questionAnswerRepository.save(qa18);
        questionAnswerRepository.save(qa19);
        questionAnswerRepository.save(qa20);
        questionAnswerRepository.save(qa21);
        questionAnswerRepository.save(qa22);
        return save;
    }

    public ProductResponse updateProduct(ProductDtos request) {
        var totalQuantity = 0;
        List<String> sizes = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        List<String> sizesadd = new ArrayList<>();
        List<String> colorsadd = new ArrayList<>();

        boolean addSize = false;
        boolean addColor = false;

        boolean xoaSize = true;
        boolean xoaColor = true;

        var product_flag = productRepository.findByID_(request.getId());

        if(product_flag != null){
            product_flag.setName(request.getName());
            product_flag.setDescription(request.getDescription());
            product_flag.setCategory(request.getCategory());
            product_flag.setTradeMask(request.getTradeMask());
            product_flag.setStyle(request.getStyle());
            product_flag.setMaterial(request.getMaterial());
            product_flag.setSubCategory(request.getSubCategory());
            product_flag.setNRating(0);

            var finalPrice = 0.0f;
            if(request.getSaleDiscountPercent() > 0){
//                product_flag.setSaleStatus(true);
                product_flag.setOriginalPrice(request.getOriginalPrice());
                product_flag.setSaleDiscountPercent(request.getSaleDiscountPercent());
                finalPrice = request.getOriginalPrice() - request.getOriginalPrice()* (request.getSaleDiscountPercent()/100);
            }else{
//                product_flag.setSaleStatus(false);
                finalPrice = request.getOriginalPrice();
            }
            product_flag.setFinalPrice(finalPrice);
            if(product_flag.getQuantitySold() != null && product_flag.getQuantitySold() >16){
                product_flag.setNewStatus(false);
            }else {
                product_flag.setNewStatus(true);
            }
            product_flag.setUpdateDate(new Date());
            product_flag.setCreatedByUserid(request.getCreatedByUserid());
        }
//        Update có sẵn
        var quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(product_flag.getName());
        if(request.getQuantityDetails() != null) {
            if (request.getQuantityDetails() != null &&
                    request.getQuantityDetails().size() > 0 &&
                    request.getQuantityDetails().size() >= quantityDetailsList.size()
            ) {
                for (var x : request.getQuantityDetails()) {
                    List<QuantityDetail> Box = new ArrayList<>();
                    for (var x_0 : quantityDetailsList) {
                        if (x.getColor().equals(x_0.getColor())) {
                            List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
                            if (!colors.contains(x.getColor())) {
                                colors.add(x.getColor());
                            }
                            var detailSizeQuantitys = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x.getColor(),product_flag.getName());
                            if (detailSizeQuantitys.size() < x.getSizes().size()) {
                                for (var y : x.getSizes()) {
                                    System.out.println(y.getSize());
                                    for (var y_0 : detailSizeQuantitys) {
                                        if (y_0.getSize().equals(y.getSize()) && x.getColor().equals(x.getColor())) {
                                            y_0.setQuantity(y.getQuantity());
                                            detailSizeQuantityRepository.save(y_0);
//                                            totalQuantity += y.getQuantity();

                                            if (!sizes.contains(y.getSize())) {
                                                sizes.add(y_0.getSize());
                                            }
                                        }
                                    }
                                    if (!sizesadd.contains(y.getSize())) {
                                        sizesadd.add(y.getSize());
                                    }
                                    if (sizesadd.size() > sizes.size()) {
                                        addSize = true;
                                        xoaSize = false;
                                    }
                                    sizeQuantities.add(SubSizeQuantity.builder()
                                            .size(y.getSize())
                                            .quantity(y.getQuantity())
                                            .build());
                                }
                            } else {
                                for (var y : detailSizeQuantitys) {
                                    for (var y_0 : x.getSizes()) {
                                        if (y.getSize().equals(y_0.getSize()) && x.getColor().equals(x.getColor())) {

                                            y.setQuantity(y_0.getQuantity());
                                            detailSizeQuantityRepository.save(y);
//                                            totalQuantity += y_0.getQuantity();

                                            if (!sizes.contains(y_0.getSize())) {
                                                sizes.add(y_0.getSize());
                                            }
                                        }
                                    }
                                    if (!sizesadd.contains(y.getSize())) {
                                        sizesadd.add(y.getSize());
                                    }
                                }
                            }
                        }
                        if (!colorsadd.contains(x.getColor())) {
                            colorsadd.add(x.getColor());
                        }
                        if (colorsadd.size() > colors.size()) {
                            addColor = true;
                            xoaColor = false;
                        }
                    }

                }

            }
            //      Kiểm Tra các trường hợp và trả về status để phần tích toán
            if (request.getQuantityDetails().size() <= quantityDetailsList.size() && !addSize) {
                for (var x : quantityDetailsList) {
                    for (var x_0 : request.getQuantityDetails()) {
                        if (x.getColor().equals(x_0.getColor())) {
                            if (!colors.contains(x_0.getColor())) {
                                colors.add(x_0.getColor());
                            }
                            var detailSizeQuantitys = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x.getColor(), x.getIdentification());
                            if (detailSizeQuantitys.size() < x_0.getSizes().size()) {
                                for (var y : x_0.getSizes()) {
                                    for (var y_0 : detailSizeQuantitys) {
                                        if (y_0.getSize().equals(y.getSize()) && x.getColor().equals(x_0.getColor())) {
                                            y_0.setQuantity(y.getQuantity());
                                            detailSizeQuantityRepository.save(y_0);
                                            totalQuantity += y.getQuantity();

                                            if (!sizes.contains(y.getSize())) {
                                                sizes.add(y_0.getSize());
                                            }
                                        }
                                    }
                                    if (!sizesadd.contains(y.getSize())) {
                                        sizesadd.add(y.getSize());
                                    }
                                    if (sizesadd.size() > sizes.size()) {
                                        addSize = true;
                                        xoaSize = false;
                                    }
                                }
                            } else {
                                for (var y : detailSizeQuantitys) {
                                    for (var y_0 : x_0.getSizes()) {
                                        if (y.getSize().equals(y_0.getSize()) && x.getColor().equals(x_0.getColor())) {
                                            y.setQuantity(y_0.getQuantity());
                                            detailSizeQuantityRepository.save(y);
                                            totalQuantity += y_0.getQuantity();

                                            if (!sizes.contains(y_0.getSize())) {
                                                sizes.add(y_0.getSize());
                                            }
                                        }
                                    }
                                    if (!sizesadd.contains(y.getSize())) {
                                        sizesadd.add(y.getSize());
                                    }
                                }
                            }
                        }
                        if (!colorsadd.contains(x.getColor())) {
                            colorsadd.add(x.getColor());
                        }
                        if (colorsadd.size() < colors.size()) {
                            addColor = true;
                            xoaColor = false;
                        }
                    }
                }
            }
            //      Log ra Status
            var colorchange = valueadd(colors, colorsadd);
            var sizechange = valueadd(sizesadd, sizes);
            //      Xóa đi nếu có
            if ((xoaColor || xoaSize)) {
                if (request.getQuantityDetails() != null && !request.getQuantityDetails().isEmpty()) {
                    for (var x : quantityDetailsList) {
                        var detailSizeQuantitys = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x.getColor(),x.getIdentification());
                        if (colorchange.contains(x.getColor()) && xoaColor) {
                            quantityDetailRepository.deleteByIdentificationAndColor(product_flag.getName(), x.getColor());
                            for (var y : detailSizeQuantitys) {
                                detailSizeQuantityRepository.deleteByIdentificationAndSizeName(x.getColor(),product_flag.getName(), y.getSize());
                            }
                        }
                        for (var y : detailSizeQuantitys) {
                            if (sizechange.contains(y.getSize()) && xoaSize) {
                                detailSizeQuantityRepository.deleteByIdentificationAndSizeName(x.getColor(),product_flag.getName(), y.getSize());
                            }
                        }
                    }
                }
            }

            //        Update Thêm nếu có
            if ((addColor || addSize)) {
                if (request.getQuantityDetails() != null && request.getQuantityDetails().size() > 0) {
                    for (var x : request.getQuantityDetails()) {
                        QuantityDetail Item = new QuantityDetail();
                        if (colorchange.contains(x.getColor()) && addColor) {
                            for (var y : x.getSizes()) {
                                totalQuantity += y.getQuantity();
                                DetailSizeQuantity detailSizeQuantity = new DetailSizeQuantity();
                                detailSizeQuantity.setSize(y.getSize());
                                detailSizeQuantity.setQuantity(y.getQuantity());
                                detailSizeQuantity.setIdentification(x.getColor());
                                detailSizeQuantity.setIdentification_pro(product_flag.getName());
                                detailSizeQuantityRepository.save(detailSizeQuantity);
                            }
                            quantityDetailsList.add(Item);
                            Item.setColor(x.getColor());
                            Item.setIdentification(product_flag.getName());
                            quantityDetailRepository.save(Item);
                        }

                        for (var y : x.getSizes()) {
                            if (sizechange.contains(y.getSize()) && addSize) {
                                var check = detailSizeQuantityRepository.DetailByIdentificationAndSizeName(x.getColor(),product_flag.getName(), y.getSize());
                                if (check != null) {
                                    continue;
                                }
                                DetailSizeQuantity detailSizeQuantity = new DetailSizeQuantity();
                                detailSizeQuantity.setSize(y.getSize());
                                detailSizeQuantity.setQuantity(y.getQuantity());
                                detailSizeQuantity.setIdentification(x.getColor());
                                detailSizeQuantity.setIdentification_pro(product_flag.getName());
                                detailSizeQuantityRepository.save(detailSizeQuantity);
                                totalQuantity += y.getQuantity();
                                if (!sizes.contains(y.getSize())) {
                                    sizes.add(y.getSize());
                                }
                            }
                        }
                    }

                }
            }
        }
        //      Thêm Ảnh mới
        List<String> productImagesBox = new ArrayList<>();
        List<ProductImg> productImagesBox_0 = productImgRepository.findProductImgByProductName(product_flag.getName());
        System.out.println(productImagesBox_0.size());
        if(productImagesBox_0!=null){
            if (request.getImages() != null && !request.getImages().isEmpty()) {
                for (var x = 0; x< request.getImages().size() ;x++) {
                    for(var y =0 ; y< productImagesBox_0.size() ;y++){
                        if(x == y){
                            try {
                                productImagesBox_0.get(y).setImage(request.getImages().get(y).getBytes());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            productImagesBox_0.get(y).setIdentification(product_flag.getName());
                            productImagesBox_0.get(y).setImageName(request.getImages().get(y).getOriginalFilename());
                            productImagesBox_0.get(y).setImg_url(ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path(Constants.Global_Image_Path)
                                    .path(product_flag.getName()+"/")
                                    .path(request.getImages().get(y).getOriginalFilename())
                                    .toUriString());
                            productImgRepository.save(productImagesBox_0.get(y));
                            if(Objects.equals(productImagesBox_0.get(y).getImageName(), request.getImageMain())){
                                product_flag.setImageMain(productImagesBox_0.get(y).getImg_url());
                            }
                            productImagesBox.add(productImagesBox_0.get(y).getImg_url());
                            productImagesBox_0.add(productImagesBox_0.get(y));
                        }

                    }
                }
            }
        }

        product_flag.setImages(productImagesBox_0);
        product_flag.setTotalQuantity(totalQuantity);
        product_flag.setQuantityDetails(quantityDetailsList);
        List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIdentification(product_flag.getName());
        List<Integer> favouriteListRe = new ArrayList<>();
        for (var x_0: favouriteList) { favouriteListRe.add(x_0.getUser_id());}

        productRepository.save(product_flag);
        // Lấy Respone
        quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(product_flag.getName());
        List<SubColor> BoxResponse = new ArrayList<>();
        for(var x :quantityDetailsList){
            List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
            var details =detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x.getColor(),x.getIdentification());
            for (var y :details){
                sizeQuantities.add(SubSizeQuantity.builder()
                        .size(y.getSize())
                        .quantity(y.getQuantity())
                        .build());
            }

            BoxResponse.add(SubColor.builder()
                    .color(x.getColor())
                    .sizes(sizeQuantities)
                    .build());
        }
        var save = ProductResponse.builder()
                .id(product_flag.getId())
                .images(productImagesBox)
                .quantitySold(product_flag.getQuantitySold())
                .style(product_flag.getStyle())
                .material(product_flag.getMaterial())
                .subCategory(request.getSubCategory())
                .sizes(valuesave(sizesadd,sizes))
                .colours(valuesave(colorsadd,colors))
                .totalQuantity(totalQuantity)
                .imageMain(product_flag.getImageMain())
                .name(product_flag.getName())
                .tradeMask(product_flag.getTradeMask())
                .description(product_flag.getDescription())
                .quantityDetails(BoxResponse)
                .category(product_flag.getCategory())
                .rating(product_flag.getRating())
                .nRating(product_flag.getNRating())
                .favourite(favouriteListRe)
                .originalPrice(product_flag.getOriginalPrice())
                .saleDiscountPercent(product_flag.getSaleDiscountPercent())
                .finalPrice(product_flag.getFinalPrice())
                .saleStatus(product_flag.getSaleStatus())
                .newStatus(product_flag.getNewStatus())
                .comments(product_flag.getComments())
                .createDate(product_flag.getCreateDate())
                .createdByUserid(product_flag.getCreatedByUserid())
                .build();
        var oriqe4 = ("Sản phẩm "+ save.getName()+" có còn hàng không?");
        var oriqe3 = ("Hiện tại "+ save.getName()+" có chương trình giảm giá hoặc khuyến mãi nào không?");
        var oriqe8 = ("Màu sắc của "+ save.getName()+" có những lựa chọn nào ?");
        var oriqe9 = ("Kích cỡ của "+ save.getName()+" có những lựa chọn nào ?");
        var oriqe10 = ("Số lượng chi tiết của "+ save.getName());
        var oriqe13 = ("Giá gốc của "+ save.getName()+" là bao nhiêu?");
        var oriqe14 = ("Giá hiện tại của "+ save.getName()+" là bao nhiêu?");

        //info
        QuestionAnswer qa0 = questionAnswerRepository.findByQuestion(product_flag.getSkuQa().toLowerCase());
        qa0.setAnswer(saveInfoChatBotAnswer(save,sizes,colors,BoxResponse));
        QuestionAnswer qa4 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe4.toLowerCase()));
        qa4.setAnswer("Tổng hàng tồn kho của "+ save.getName()+" còn "+save.getTotalQuantity()+" sản phẩm các loại, Để kiểm tra tình trạng hàng tồn kho chi tiết, Bạn vui lòng nhấn vào trang sản phẩm của chúng tôi. ");
        QuestionAnswer qa3 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe3.toLowerCase()));
        if(save.getSaleDiscountPercent() !=0) {
            qa3.setAnswer("Hiện tại, chúng tôi đang có chương trình khuyến mãi "+ save.getSaleDiscountPercent()+"% cho "+ save.getName()+". Bạn có thể tìm hiểu thêm tại trang khuyến mãi của chúng tôi.");
        }else {
            qa3.setAnswer("Hiện tại, chúng tôi không có chương trình khuyến mãi cho "+ save.getName()+". Bạn có thể tìm hiểu thêm tại trang khuyến mãi của chúng tôi.");
        }
        // [tên sản phẩm] hiện màu sắc nào?
        QuestionAnswer qa8 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe8.toLowerCase()));
        qa8.setAnswer(savesizesAnswer(save.getName(),colors));
        // [tên sản phẩm] hiện Có kích thước nào?
        QuestionAnswer qa9 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe9.toLowerCase()));
        qa9.setAnswer(savecolorsAnswer(save.getName(),sizes));
        //Số lượng Chi tiết của  "+ namepro
        QuestionAnswer qa10 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe10.toLowerCase()));
        qa10.setAnswer(saveQuantityDetailAnswer(save,sizes,colors,BoxResponse));
        //Giá gốc của [tên sản phẩm] là bao nhiêu?
        QuestionAnswer qa13 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe13.toLowerCase()));
        qa13.setAnswer("Giá gốc của "+ save.getName()+" là "+save.getOriginalPrice() + " VND");
        // Giá khuyến mãi hiện tại của [tên sản phẩm] là bao nhiêu?
        QuestionAnswer qa14 = questionAnswerRepository.findByQuestion(removeDiacritics(oriqe14.toLowerCase()));
        qa13.setAnswer("Giá khuyến mãi hiện tại của "+ save.getName()+" là "+save.getFinalPrice() + "VND");


        questionAnswerRepository.save(qa0);
        questionAnswerRepository.save(qa3);
        questionAnswerRepository.save(qa4);
        questionAnswerRepository.save(qa8);
        questionAnswerRepository.save(qa9);
        questionAnswerRepository.save(qa10);
        questionAnswerRepository.save(qa13);
        questionAnswerRepository.save(qa14);
        return save;
    }
    public List<String> valueadd(List<String>a , List<String>b){
        List<String> save = new ArrayList<>();
        if(a.size() > b.size()){
            for (var x: a){
                if(!b.contains(x)){
                    save.add(x);
                }
            }
        }else if(a.size() < b.size()){
            for (var x: b){
                if(!a.contains(x)){
                    save.add(x);
                }
            }
        }
        return save;
    }

    public List<String> valuesave(List<String>a , List<String>b){
        List<String> save = new ArrayList<>();
            for (var x: a){
                if(b.contains(x)){
                    save.add(x);
                }
            }
        return save;
    }

    public ProductListDtos search_all(
            Integer _userId,
            Integer _limit,
            Integer _page,
            String categoryName,
            String productName,
            String style,
            String material,
            List<String> subCategoryName,
            Boolean isPromotion,
            Boolean isReleased,
            Integer ratingGte,
            Integer priceGte,
            Integer priceLte,
            String sort,
            Boolean isBestSelling,
            String removeDetail
    ) {
        int ratingLt = 6;
        int _total = 0;
        if (ratingGte != null) {
            ratingLt = ratingGte + 1;
        }
        if(productName == null){
            productName = "";
        }
        List<Product> productList = new ArrayList<>();
        if (subCategoryName != null) {
            for (var x : subCategoryName) {
                List<Product> box = productRepository.search_all(productName,style,material,categoryName, x, isPromotion, isReleased, ratingGte, ratingLt, priceGte, priceLte);
                for (var y : box) {
                    if (!productList.contains(y)) {
                        productList.add(y);
                    }
                }
            }
        } else {
            List<Product> box = productRepository.search_all(productName,style,material,categoryName, null, isPromotion, isReleased, ratingGte, ratingLt, priceGte, priceLte);
            for (var y : box) {
                if (!productList.contains(y)) {
                    productList.add(y);
                }
            }
        }

        // Apply sorting
        if ("ASC".equalsIgnoreCase(sort)) {
            productList.sort(Comparator.comparing(Product::getFinalPrice));
        } else if ("DESC".equalsIgnoreCase(sort)) {
            productList.sort(Comparator.comparing(Product::getFinalPrice).reversed());
        }else{
            productList.sort((Comparator.comparing(Product::getId)).reversed());
        }

        if (isBestSelling != null && isBestSelling) {
            productList.sort(Comparator.comparing(Product::getQuantitySold).reversed());
        }
        _total = productList.size();
        if(_page == null){
            _page = 1;
        }
        if(_limit == null){
            _limit = 20;
        }

        List<ProductResponse> productsResponseList = new ArrayList<>();
        for (var x : productList) {
            if(removeDetail!=null && Objects.equals(x.getName(), removeDetail)){
                continue;
            }
            var unactiveStyle = styleRepository.findByUnActive();
            var unactiveCategory = categoryRepository.findByUnActive();
            var unactiveMaterial = materialRepository.findByUnActive();
            var unactiveTradeMask = tradeMarkRepository.findByUnActive();
            List<String> unactiveStyle_ = new ArrayList<>();
            List<String> unactiveCategory_ = new ArrayList<>();
            List<String> unactiveMaterial_ = new ArrayList<>();
            List<String> unactiveTradeMask_ = new ArrayList<>();
            for (var flag: unactiveStyle) {unactiveStyle_.add(flag.getName());}
            for (var flag: unactiveCategory) {unactiveCategory_.add(flag.getName());}
            for (var flag: unactiveMaterial) {unactiveMaterial_.add(flag.getName());}
            for (var flag: unactiveTradeMask) {unactiveTradeMask_.add(flag.getName());}
            if(unactiveStyle_.contains(x.getStyle())){continue;}
            if(unactiveCategory_.contains(x.getCategory())){continue;}
            if(unactiveMaterial_.contains(x.getMaterial())){continue;}
            if(unactiveTradeMask_.contains(x.getTradeMask())){continue;}

            List<ProductImg> imgs = productImgRepository.findProductImgByProductName(x.getName());
            List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(x.getName());
            List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(x.getName());
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIdentification(x.getName());



            List<String> productImagesBox = new ArrayList<>();
            List<Integer> favouriteListRe = new ArrayList<>();
            List<String> colorsListRe = new ArrayList<>();
            List<String> sizeListRe = new ArrayList<>();
            List<SubColor> subColors = new ArrayList<>();
            boolean isFavourite = false;
            for (var x_0 : quantityDetailsList) {
                List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x_0.getColor(), x_0.getIdentification());
                if (!colorsListRe.contains(x_0.getColor())) {
                    colorsListRe.add(x_0.getColor());
                }
                List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
                for (var y_0 : detailSizeQuantities) {
                    if (!sizeListRe.contains(y_0.getSize())) {
                        sizeListRe.add(y_0.getSize());
                    }
                    if (y_0.getIdentification().equals(x_0.getColor())) {
                        sizeQuantities.add(SubSizeQuantity.builder()
                                .size(y_0.getSize())
                                .quantity(y_0.getQuantity())
                                .build());
                    }
                }
                subColors.add(SubColor.builder()
                        .color(x_0.getColor())
                        .sizes(sizeQuantities)
                        .build());
                x_0.setSizeQuantities(detailSizeQuantities);
            }

            Float rating = 0.0f;
            int nRating = commentsList.size();
            for (var x_0 : commentsList) {
                rating += x_0.getRating();
            }
            rating = nRating > 0 ? rating / nRating : 0;

            for (var x_0 : favouriteList) {
                favouriteListRe.add(x_0.getUser_id());
            }
            if(favouriteListRe.contains(_userId)){
                isFavourite = true;
            }
            for (var x_0 : imgs) {
                if(!Objects.equals(x.getImageMain(), x_0.getImg_url())){
                    productImagesBox.add(x_0.getImg_url());
                }
            }
            var y = ProductResponse.builder()
                    .id(x.getId())
                    .isFavorite(isFavourite)
                    .imageMain(x.getImageMain())
                    .skuQa(x.getSkuQa())
                    .images(productImagesBox)
                    .subCategory(x.getSubCategory())
                    .sizes(sizeListRe)
                    .quantitySold(x.getQuantitySold())
                    .style(x.getStyle())
                    .material(x.getMaterial())
                    .colours(colorsListRe)
                    .name(x.getName())
                    .description(x.getDescription())
                    .quantityDetails(subColors)
                    .totalQuantity(x.getTotalQuantity())
                    .category(x.getCategory())
                    .rating(rating)
                    .nRating(nRating)
                    .tradeMask(x.getTradeMask())
                    .favourite(favouriteListRe)
                    .originalPrice(x.getOriginalPrice())
                    .saleDiscountPercent(x.getSaleDiscountPercent())
                    .finalPrice(x.getFinalPrice())
                    .saleStatus(x.getSaleStatus())
                    .newStatus(x.getNewStatus())
                    .comments(commentsList)
                    .createDate(x.getCreateDate())
                    .createdByUserid(x.getCreatedByUserid())
                    .build();
            productsResponseList.add(y);
        }
        List<ProductResponse> paginatedProducts = new ArrayList<>();
        if(removeDetail != null){
            Collections.shuffle(productsResponseList);
            int start = Math.max((_page - 1) * _limit, 0);
            int end = Math.min(start + _limit, productList.size());
            paginatedProducts = productsResponseList.subList(start, end);
//            Collections.shuffle(paginatedProducts);
        }else {
            int start = Math.max((_page - 1) * _limit, 0);
            int end = Math.min(start + _limit, productList.size());
            paginatedProducts = productsResponseList.subList(start, end);
        }
        return ProductListDtos.builder().data(paginatedProducts).paginationDto(new PaginationDto(_limit,_page,_total)).build();
    }

    public ProductResponse findbyIDHander(Integer _userId,Integer id){
        var boxItem = productRepository.findById(id);
        List<ProductImg> imgs = productImgRepository.findProductImgByProductName(boxItem.get().getName());
        List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(boxItem.get().getName());
        List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(boxItem.get().getName());
        List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIdentification(boxItem.get().getName());

//        List<SubImgResponse> productImagesBox = new ArrayList<>();
        List<String> productImagesBox = new ArrayList<>();
        List<SubColor> subColors = new ArrayList<>();
        List<Integer> favouriteListRe = new ArrayList<>();
        List<String> colorsListRe = new ArrayList<>();
        List<String> sizesListRe = new ArrayList<>();
        Float rating = 0.0f;
        int nRating = commentsList.size();
        if(nRating != 0.0f){
            for (var x_0: commentsList) {
                rating += x_0.getRating();
            }
            rating = rating/nRating;
        }else {
            rating = 5.0f;
        }
        boolean isFavourite = false;
        for (var x_0: favouriteList) { favouriteListRe.add(x_0.getUser_id());}
        if(favouriteListRe.contains(_userId)){
            isFavourite = true;
        }
        for (var x_0: quantityDetailsList) {
            List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x_0.getColor(),x_0.getIdentification());
            if(!colorsListRe.contains(x_0.getColor())){
                colorsListRe.add(x_0.getColor());
            }
            List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
            for (var y_0: detailSizeQuantities){
                if(!sizesListRe.contains(y_0.getSize())){
                    sizesListRe.add(y_0.getSize());
                }
                if(y_0.getIdentification().equals(x_0.getColor())){
                    sizeQuantities.add(SubSizeQuantity.builder()
                            .size(y_0.getSize())
                            .quantity(y_0.getQuantity())
                            .build());
                }
            }
            subColors.add(SubColor.builder()
                    .color(x_0.getColor())
                    .sizes(sizeQuantities)
                    .build());
            x_0.setSizeQuantities(detailSizeQuantities);
        }
        for (var x_0: imgs) {
            if(!Objects.equals(boxItem.get().getImageMain(), x_0.getImg_url())){
                productImagesBox.add(x_0.getImg_url());
            }
        }
        var productResponse = ProductResponse.builder()
                .id(boxItem.get().getId())
                .isFavorite(isFavourite)
                .skuQa(boxItem.get().getSkuQa())
                .images(productImagesBox)
                .name(boxItem.get().getName())
                .sizes(sizesListRe)
                .quantitySold(boxItem.get().getQuantitySold())
                .style(boxItem.get().getStyle())
                .material(boxItem.get().getMaterial())
                .colours(colorsListRe)
                .imageMain(boxItem.get().getImageMain())
                .subCategory(boxItem.get().getSubCategory())
                .totalQuantity(boxItem.get().getTotalQuantity())
                .description(boxItem.get().getDescription())
                .quantityDetails(subColors)
                .category(boxItem.get().getCategory())
                .rating(rating)
                .tradeMask(boxItem.get().getTradeMask())
                .nRating(nRating)
                .favourite(favouriteListRe)
                .originalPrice(boxItem.get().getOriginalPrice())
                .saleDiscountPercent(boxItem.get().getSaleDiscountPercent())
                .finalPrice(boxItem.get().getFinalPrice())
                .saleStatus(boxItem.get().getSaleStatus())
                .newStatus(boxItem.get().getNewStatus())
                .comments(commentsList)
                .createDate(boxItem.get().getCreateDate())
                .createdByUserid(boxItem.get().getCreatedByUserid())
                .build();
        return productResponse;
    }


    public ResponseMessageDtos removebyId(Integer id) {
        var boxItem = productRepository.findById(id);
        if(!boxItem.isPresent()){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Delete_Product_Fail)
                    .build();
        }
        productImgRepository.deleteByIdentification(boxItem.get().getName());
        commentRepository.deleteByIdentification(boxItem.get().getName());
        List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(boxItem.get().getName());
        for (var x_0: quantityDetailsList) {
            List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x_0.getColor(),x_0.getIdentification());
            for (var y_0: detailSizeQuantities){
                if(y_0.getIdentification().equals(x_0.getColor())){
                    detailSizeQuantityRepository.deleteByIdentification(x_0.getColor());
                }
            }
            x_0.setSizeQuantities(detailSizeQuantities);
        }
        quantityDetailRepository.deleteByIdentification(boxItem.get().getName());
        favouriteProductRepository.deleteByIdentification(boxItem.get().getName());
        productRepository.deleteById(id);
        return ResponseMessageDtos.builder()
                .status(200)
                .message(Constants.Delete_Product_Success)
                .build();
    }
//-------------------comments-----------------------------------
    public List<Comment> listCommentByIdentification_pro (String identification)
            throws ChangeSetPersister.NotFoundException
    {
        List<Comment> commentList =  commentRepository.findCommentByIAndIdentification_pro(identification);
        return commentList;
    }

    public List<Comment> listCommentByIdentification_user (String identification)
            throws ChangeSetPersister.NotFoundException
    {
        List<Comment> commentList =  commentRepository.findCommentByIAndIdentification_user(identification);
        return commentList;
    }
    public List<Comment> listComment ()
            throws ChangeSetPersister.NotFoundException
    {
        List<Comment> commentList =  commentRepository.findAll();
        return commentList;
    }

    public List<Comment> unfinishedComment (String user)
            throws ChangeSetPersister.NotFoundException
    {
        List<Comment> commentList =  commentRepository.findCommentUnfinished(user);
        return commentList;
    }

    public List<Comment> completeComment (String user)
            throws ChangeSetPersister.NotFoundException
    {
        List<Comment> commentList =  commentRepository.findCommentCompleted(user);
        return commentList;
    }
    public Comment createComment (CommentDto commentDto,Integer product_id)
            throws ChangeSetPersister.NotFoundException
    {
        Product product = productRepository.findById(
                product_id).orElseThrow(()-> new ChangeSetPersister.NotFoundException());
        List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(product.getName());
        var comment_status = commentDto.getStatus();
        if(comment_status == null){
            comment_status =0;
        }
        var rate_status = true;
        if(commentDto.getContent() == null ){
            rate_status = false;
        }
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .rating(commentDto.getRating())
                .color(commentDto.getColor())
                .size(commentDto.getSize())
                .statusActive(comment_status)
                .image(product.getImageMain())
                .identification_order(commentDto.getIdOrder())
                .imageUser(userRepository.findByName_(commentDto.getUsername()).getUserImgUrl())
                .identification_pro(product.getName())
                .rate_status(rate_status)
                .createDate(new Date())
                .identification_user(commentDto.getUsername())
                .build();
        commentsList.add(comment);
        Float rating = 0.0f;
        int nRating = commentsList.size();
        for (var x_0: commentsList) {
            rating += x_0.getRating();
        }
        rating = rating/nRating;
        product.setRating(rating);
        //chất lượng của [tên sản phẩm] được đánh giá thế nào?
        QuestionAnswer qa12;
        if(questionAnswerRepository.findByQuestion("Người dùng trước đây có đánh giá như thế nào về "+ product.getName()) !=null){
            qa12 = questionAnswerRepository.findByQuestion("Người dùng trước đây có đánh giá như thế nào về "+ product.getName());
            qa12.setAnswer(rating(product.getName(),rating,nRating));
        }else{
            qa12 = QuestionAnswer.builder()
                    .question(removeDiacritics(("Người dùng trước đây có đánh giá như thế nào về "+ product.getName()).toLowerCase()))
                    .answer(rating(product.getName(),rating,nRating))
                    .build();
        }
        originalQuestionRepository.save(OriginalQuestion.builder().question("Người dùng trước đây có đánh giá như thế nào về "+ product.getName()).build());
        questionAnswerRepository.save(qa12);
        commentRepository.save(comment);
        productRepository.save(product);
        return comment;
    }

    public ResponseMessageDtos updateComment (CommentDto commentDto,Integer comment_id)
            throws ChangeSetPersister.NotFoundException
    {
        Comment comment = commentRepository.findCommentById(comment_id);
        List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(comment.getIdentification_pro());
        Product product = productRepository.findByName_(comment.getIdentification_pro());

        if(comment == null){
            return ResponseMessageDtos.builder()
                    .status(400)
                    .message(Constants.Comment_Not_Found)
                    .build();
        }
        if(!Objects.equals(comment.getIdentification_user(), commentDto.getUsername())){
            return ResponseMessageDtos.builder()
                    .status(400)
                    .message(Constants.Comment_Permission_Fail)
                    .build();
        }
        comment.setRate_status(true);
        comment.setContent(commentDto.getContent());
        comment.setRating(commentDto.getRating());
        if(comment.getContent() == null){
            comment.setRate_status(false);
        }
        commentsList.add(comment);
        Float rating = 0.0f;
        int nRating = commentsList.size();
        for (var x_0: commentsList) {
            rating += x_0.getRating();
        }
        rating = rating/nRating;
        product.setRating(rating);
        commentRepository.save(comment);
        productRepository.save(product);
        return ResponseMessageDtos.builder()
                .status(200)
                .message(Constants.Update_Comment_Success)
                .build();
    }

    public ResponseMessageDtos deleteCommentDto (Integer comment_id)
            throws ChangeSetPersister.NotFoundException
    {
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(()-> new ChangeSetPersister.NotFoundException());
        if(comment == null){
            return ResponseMessageDtos.builder()
                    .status(404)
                    .message(Constants.Delete_Comment_Fail)
                    .build();
        }
        commentRepository.delete(comment);
        return ResponseMessageDtos.builder()
                .status(200)
                .message(Constants.Delete_Comment_Success)
                .build();
    }
//-------------------------------------------Favourite-----------------------------------------------------------------
    public List<ProductResponse> findbyFavouriteByUserID(Integer id){
        List<ProductResponse> boxProduct = new ArrayList<>();
        var boxItems = productRepository.findAll();
        for (var x : boxItems) {
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIdentification(x.getName());
            List<Integer> favouriteListRe = new ArrayList<>();
            for (var x_0: favouriteList) { favouriteListRe.add(x_0.getUser_id());}
            if(favouriteListRe.contains(id)){
                List<ProductImg> imgs = productImgRepository.findProductImgByProductName(x.getName());
                List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(x.getName());
                List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(x.getName());

                List<String> productImagesBox = new ArrayList<>();
                List<SubColor> subColors = new ArrayList<>();
                List<String> colorsListRe = new ArrayList<>();
                List<String> sizesListRe = new ArrayList<>();
                for (var x_0: quantityDetailsList) {
                    List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x_0.getColor(),x_0.getIdentification());
                    if(!colorsListRe.contains(x_0.getColor())){
                        colorsListRe.add(x_0.getColor());
                    }
                    List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
                    for (var y_0: detailSizeQuantities){
                        if(!sizesListRe.contains(y_0.getSize())){
                            sizesListRe.add(y_0.getSize());
                        }
                        if(y_0.getIdentification().equals(x_0.getColor())){
                            sizeQuantities.add(SubSizeQuantity.builder()
                                    .size(y_0.getSize())
                                    .quantity(y_0.getQuantity())
                                    .build());
                        }
                    }
                    subColors.add(SubColor.builder()
                            .color(x_0.getColor())
                            .sizes(sizeQuantities)
                            .build());
                    x_0.setSizeQuantities(detailSizeQuantities);
                }
                Float rating = 0.0f;
                int nRating = commentsList.size();
                for (var x_0: commentsList) {
                    rating += x_0.getRating();
                }
                rating = rating/nRating;
                for (var x_0: imgs) {
//                    SubImgResponse response = SubImgResponse.builder()
//                            .id(x_0.getId())
//                            .img_url(x_0.getImg_url())
//                            .imageName(x_0.getImageName())
//                            .identification(x_0.getIdentification())
//                            .build();
                    productImagesBox.add(x_0.getImg_url());
                }
                var productResponse = ProductResponse.builder()
                        .id(x.getId())
                        .skuQa(x.getSkuQa())
                        .isFavorite(true)
                        .imageMain(x.getImageMain())
                        .images(productImagesBox)
                        .sizes(sizesListRe)
                        .quantitySold(x.getQuantitySold())
                        .style(x.getStyle())
                        .material(x.getMaterial())
                        .colours(colorsListRe)
                        .subCategory(x.getSubCategory())
                        .name(x.getName())
                        .tradeMask(x.getTradeMask())
                        .description(x.getDescription())
                        .quantityDetails(subColors)
                        .category(x.getCategory())
                        .rating(rating)
                        .nRating(nRating)
                        .favourite(favouriteListRe)
                        .totalQuantity(x.getTotalQuantity())
                        .originalPrice(x.getOriginalPrice())
                        .saleDiscountPercent(x.getSaleDiscountPercent())
                        .finalPrice(x.getFinalPrice())
                        .saleStatus(x.getSaleStatus())

                        .newStatus(x.getNewStatus())
                        .comments(commentsList)
                        .createDate(x.getCreateDate())
                        .createdByUserid(x.getCreatedByUserid())
                        .build();
                boxProduct.add(productResponse);
            }
        }

        return boxProduct;
    }
    public ResponseMessageDtos addFavourite(String product_name,Integer user_id) {

        var product_flag = productRepository.findByName_(product_name);
        if(product_flag != null){
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIdentification(product_name);
            FavouriteProduct newF = FavouriteProduct.builder()
                    .user_id(user_id)
                    .identification(product_name)
                    .build();
            favouriteList.add(newF);
            favouriteProductRepository.save(newF);
            product_flag.setFavourite(favouriteList);
            productRepository.save(product_flag);
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Add_Favorite_Success)
                    .build();
        }
        return ResponseMessageDtos.builder()
                .status(404)
                .message(Constants.Add_Favorite_Fail)
                .build();
    }

    public ResponseMessageDtos deleteFavourite(String product_name,Integer user_id) {

        var product_flag = productRepository.findByName_(product_name);
        if(product_flag != null){
            favouriteProductRepository.deleteByUser_id(user_id,product_name);
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIdentification(product_name);
            product_flag.setFavourite(favouriteList);
            productRepository.save(product_flag);
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Delete_Favorite_Success)
                    .build();
        }
        return ResponseMessageDtos.builder()
                .status(404)
                .message(Constants.Delete_Favorite_Fail)
                .build();
    }

    public ResponseMessageDtos deleteAllFavourite(Integer user_id) {

        var user = userRepository.findById(user_id);
        if(user.isPresent()){
            favouriteProductRepository.deleteAllByUser_id(user_id);
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Delete_Favorite_Success)
                    .build();
        }
        return ResponseMessageDtos.builder()
                .status(404)
                .message(Constants.Delete_Favorite_Fail)
                .build();
    }
//----------------------------------------------------------------------------------------------------


    public SubColorDetailResponse getSizeQuantityByColorAndproductname(String productName, String color){
        var boxListColor = quantityDetailRepository.findQuantityDetailsByIAndIdentificationAndColor(productName,color);
        if(boxListColor == null){
            return SubColorDetailResponse.builder()
                    .color(color)
                    .sizes(null)
                    .message(Constants.Color_Detail_Not_Found)
                    .build();
        }
        List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(boxListColor.getColor(),boxListColor.getIdentification());
        if(detailSizeQuantities == null){
            return SubColorDetailResponse.builder()
                    .color(color)
                    .sizes(null)
                    .message(Constants.Fetch_Data_Colors_QuantityDetail_Fail)
                    .build();
        }
        List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
            for (var y_0: detailSizeQuantities){
                if(y_0.getIdentification().equals(color)){
                    sizeQuantities.add(SubSizeQuantity.builder()
                            .size(y_0.getSize())
                            .quantity(y_0.getQuantity())
                            .build());
                }
            }
        return SubColorDetailResponse.builder()
                .color(color)
                .sizes(sizeQuantities)
                .message(Constants.Fetch_Data_Colors_QuantityDetail_Success)
                .build();
    }

    public SubQuantityTotalResponse getQuantityTotal(String productName){
        List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(productName);
        if(quantityDetailsList == null){
            return SubQuantityTotalResponse.builder()
                    .quantityDetails(null)
                    .message(Constants.Fetch_Data_Quantity_Total_Fail)
                    .build();
        }
        List<SubColor> subColors = new ArrayList<>();
        for (var x_0: quantityDetailsList) {
            List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x_0.getColor(),x_0.getIdentification());
            if(detailSizeQuantities == null){
                return SubQuantityTotalResponse.builder()
                        .quantityDetails(null)
                        .message(Constants.List_Color_Detail_Not_Found)
                        .build();
            }
            List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
            for (var y_0: detailSizeQuantities){
                if(y_0.getIdentification().equals(x_0.getColor())){
                    sizeQuantities.add(SubSizeQuantity.builder()
                            .size(y_0.getSize())
                            .quantity(y_0.getQuantity())
                            .build());
                }
            }
            subColors.add(SubColor.builder()
                    .color(x_0.getColor())
                    .sizes(sizeQuantities)
                    .build());
            x_0.setSizeQuantities(detailSizeQuantities);
        }
        return SubQuantityTotalResponse.builder()
                .quantityDetails(subColors)
                .message(Constants.Fetch_Data_Quantity_Total_Success)
                .build();
    }
//    ----------------------------------TradeMask------------------------------------------
        public List<TradeMark> findAllTradeMark() {
            return tradeMarkRepository.findAll();
        }

        public List<TradeMark> findAllTradeMarkActive() {
            return tradeMarkRepository.findByActive();
        }

        public TradeMark findByTradeMarkId(String name) {
            return tradeMarkRepository.findByName(name);
        }

        public TradeMark saveTrask(TradeMaskDtos tradeMark) {
            if(tradeMarkRepository.findByName(tradeMark.getTradeMarkName())==null){
                var save = TradeMark.builder()
                        .sku(tradeMark.getSku())
                        .name(tradeMark.getTradeMarkName())
                        .image_url(tradeMark.getImage_url())
                        .status(tradeMark.getStatus())
                        .description(tradeMark.getDescription())
                        .createDate(new Date())
                        .build();
                return tradeMarkRepository.save(save);
            }
            return null;
        }
        public TradeMark updateTrask(TradeMark tradeMark) {
            return tradeMarkRepository.save(tradeMark);
        }
        public void deleteById(Integer id) {
            tradeMarkRepository.deleteById(id);
        }
//    ----------------------------------Style------------------------------------------
    public List<Style> getAllStyles() {
        return styleRepository.findAll();
    }
    public List<Style> getAllStylesActive() {
        return styleRepository.findByActive();
    }
    public Style getStyleById(String name) {
        return styleRepository.findByName(name);
    }
    public Style createStyle(StyleDtos style) {
        if(styleRepository.findByName(style.getStyleName())==null) {
            var save = Style.builder()
                    .name(style.getStyleName())
                    .image_url(style.getImage_url())
                    .status(style.getStatus())
                    .description(style.getDescription())
                    .createDate(new Date())
                    .build();
            return styleRepository.save(save);
        }
        return null;
    }

    public Style updateStyle(Style updatedStyle) {
        return styleRepository.save(updatedStyle);
    }

    public void deleteStyle(Integer id) {
        styleRepository.deleteById(id);
    }
    //    ----------------------------------Material------------------------------------------
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public List<Material> getAllMaterialsActive() {
        return materialRepository.findByActive();
    }


    public Material getMaterialById(String name) {
        return materialRepository.findByName(name);
    }

    public Material createMaterial(MaterialDtos material) {
        if(materialRepository.findByName(material.getMaterialName())==null) {
            var save = Material.builder()
                    .name(material.getMaterialName())
                    .image_url(material.getImage_url())
                    .status(material.getStatus())
                    .description(material.getDescription())
                    .createDate(new Date())
                    .build();
            return materialRepository.save(save);
        }
        return null;
    }

    public Material updateMaterial( Material updatedMaterial) {
        return materialRepository.save(updatedMaterial);
    }

    public void deleteMaterial(Integer id) {
        materialRepository.deleteById(id);
    }
// ----------------------------------------------------------------------------------------------
    public StatisticsResponse statistics(){
        var productActive =  search_all(null,10000000,1,null,null,null,null,null,null,null,null,null,null,null,true,null);
        var bestSelling =  search_all(null,4,1,null,null,null,null,null,null,null,null,null,null,null,true,null);
        if(productActive==null || bestSelling == null){
            return null;
        }
        //loc order da huy
        var orders = orderRepository.findByAllOrderActive();
        orders.removeIf(x -> Objects.equals(x.getOrderStatus(), Constants.Order_Status_Cancelled));
        var nOrders = orders.size();
        var nOrdersThisMonth = 0;
        for (var flog: orders){
            var createdAt = flog.getCreatedAt();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createdAt);
            int month = calendar.get(Calendar.MONTH);
            calendar.setTime(new Date());
            if(month == calendar.get(Calendar.MONTH)){
                nOrdersThisMonth += 1;
            }
        }
        //loc order chua thanh toan
        var revenue = 0.0f;
        var revenueThisMonth = 0.0f;
        orders.removeIf(x -> Objects.equals(x.getPaymentStatus(), 0));
        for (var flog: orders){
            revenue += flog.getTotalAmountOrder();
            var createdAt = flog.getCreatedAt();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createdAt);
            int month = calendar.get(Calendar.MONTH);
            calendar.setTime(new Date());
            if(month == calendar.get(Calendar.MONTH)){
                revenueThisMonth += flog.getTotalAmountOrder();
            }
        }
        //So luong san pham da ban
        var nSold = 0;
        for(var x:productActive.getData()){
            nSold+= x.getQuantitySold();
        }
        //loc so luong khach hang
        var users = userRepository.findALl_();
        users.removeIf(x -> x.getRole() == Role.ADMIN);

        return StatisticsResponse.builder()
                .proBestSelling(bestSelling.getData())
                .nTotalCustomer(users.size())
                .nTotalProductActive(productActive.getData().size())
                .nTotalOrders(nOrders)
                .nTotalOrdersThisMonth(nOrdersThisMonth)
                .nTotalRevenueThisMonth(revenueThisMonth)
                .nTotalRevenue(revenue)
                .nTotalQuantitySold(nSold)
                .nTotalWaitingQuestion(questionOfGuestRepository.findByQuestionUnAnsweredNUl().size())
                .nTotalRating(commentRepository.findAllCommentCompleted().size())
                .build();
    }
    public ChartData statisticsChartBar(){
        var orders = orderRepository.findAll();
        Float nOrders = (float) orders.size();
        orders.removeIf(x -> Objects.equals(x.getOrderStatus(), Constants.Order_Status_Cancelled));
        Float nDaHuy = (float) (nOrders - orders.size());
        orders.removeIf(x -> Objects.equals(x.getOrderStatus(), Constants.Order_Status_Received));
        Float nDaNhanHang = (float)(nOrders - nDaHuy - orders.size());
        orders.removeIf(x -> Objects.equals(x.getOrderStatus(), Constants.Order_Status_In_Transit));
        Float nDangVanCHuyen = (float)(nOrders - nDaNhanHang- nDaHuy - orders.size());
        orders.removeIf(x -> Objects.equals(x.getOrderStatus(), null));
        Float nDangxuly = (float)(nOrders -nDangVanCHuyen -nDaNhanHang- nDaHuy - orders.size());
        Data data0 = Data.builder()
                .year(2024)
                .labels(Arrays.asList(Constants.Order_Status_Process,Constants.Order_Status_In_Transit,Constants.Order_Status_Received,Constants.Order_Status_Cancelled))
                .data(Arrays.asList(nDangxuly,nDangVanCHuyen,nDaNhanHang,nDaHuy))
                .build();
        List<Data> list = new ArrayList<>();
        list.add(data0);
        return ChartData.builder()
                .datasets(list)
                .build();
    }
    public ChartData statisticsChartLine(){
        float nt1= 0;float nt2= 0;float nt3= 0;float nt4= 0;float nt5= 0;float nt6= 0;
        float nt7= 0;float nt8= 0;float nt9= 0;float nt10= 0;float nt11= 0;float nt12= 0;
        var orders = orderRepository.findAll();
        orders.removeIf(x -> Objects.equals(x.getOrderStatus(), Constants.Order_Status_Cancelled));
        orders.removeIf(x -> Objects.equals(x.getPaymentStatus(), 0));
        for(var x:orders){
            var createdAt = x.getCreatedAt();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createdAt);
            int month = calendar.get(Calendar.MONTH);
            switch (month) {
                case 0 -> nt1 += x.getTotalAmountOrder();
                case 1 -> nt2 += x.getTotalAmountOrder();
                case 2 -> nt3 += x.getTotalAmountOrder();
                case 3 -> nt4 += x.getTotalAmountOrder();
                case 4 -> nt5 += x.getTotalAmountOrder();
                case 5 -> nt6 += x.getTotalAmountOrder();
                case 6 -> nt7 += x.getTotalAmountOrder();
                case 7 -> nt8 += x.getTotalAmountOrder();
                case 8 -> nt9 += x.getTotalAmountOrder();
                case 9 -> nt10 += x.getTotalAmountOrder();
                case 10 -> nt11 += x.getTotalAmountOrder();
                case 11 -> nt12 += x.getTotalAmountOrder();
                default -> {
                }
            }
        }
        Data data0 = Data.builder()
                .year(2024)
                .data(Arrays.asList(nt1,nt2,nt3,nt4,nt5,nt6,nt7,nt8,nt9,nt10,nt11,nt12))
                .build();
        Data data1 = Data.builder()
                .year(2023)
                .data(Arrays.asList(5000.0f,5000.0f,5000.0f,2000.0f,8000.0f,9000.0f,5000.0f,2000.0f,8000.0f,10000.0f,5000.0f,5000.0f))
                .build();
        List<Data> list = new ArrayList<>();
        list.add(data0);
        list.add(data1);
        return ChartData.builder()
                .datasets(list)
                .build();
    }
//    -------------------------------QR info--------------------------------------------------
    public List<QRInfo> getAllQRInfos() {
        return qrInfoRepository.findAll();
    }

    public Optional<QRInfo> getQRInfoById(Integer id) {
        return qrInfoRepository.findById(id);
    }

    public QRInfo createQRInfo(QRInfoDtos qrInfo) {
        var save = qrInfoRepository.findQRAccount(qrInfo.getAccountNo(), qrInfo.getAccountName());
        if(save != null){
            return null;
        }
        var newSave =  new QRInfo();
        newSave.setAccountNo(qrInfo.getAccountNo());
        newSave.setAccountName(qrInfo.getAccountName());
        newSave.setAcqId(qrInfo.getAcqId());
        newSave.setTemplate(qrInfo.getTemplate());
        newSave.setStatus(qrInfo.getStatus());
        newSave.setCreateAt(new Date());
        newSave.setCreateBy(qrInfo.getCreateBy());
        return qrInfoRepository.save(newSave);
    }

    public QRInfo updateQRInfo(Integer id, QRInfo qrInfoDetails) {
        QRInfo qrInfo = qrInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QRInfo not found with id: " + id));

        qrInfo.setAccountNo(qrInfoDetails.getAccountNo());
        qrInfo.setAccountName(qrInfoDetails.getAccountName());
        qrInfo.setAcqId(qrInfoDetails.getAcqId());
        qrInfo.setTemplate(qrInfoDetails.getTemplate());
        qrInfo.setStatus(qrInfoDetails.getStatus());
        qrInfo.setCreateBy(qrInfoDetails.getCreateBy());

        return qrInfoRepository.save(qrInfo);
    }

    public void deleteQRInfo(Integer id) {
        qrInfoRepository.deleteById(id);
    }

}
