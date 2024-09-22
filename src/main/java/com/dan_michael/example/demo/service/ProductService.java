package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.chatbot.entities.OriginalQuestion;
import com.dan_michael.example.demo.chatbot.entities.QuestionAnswer;
import com.dan_michael.example.demo.chatbot.resository.OriginalQuestionRepository;
import com.dan_michael.example.demo.chatbot.resository.QuestionAnswerRepository;
import com.dan_michael.example.demo.model.dto.global.PaginationDto;
import com.dan_michael.example.demo.model.dto.ob.*;
import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import com.dan_michael.example.demo.model.entities.*;
import com.dan_michael.example.demo.model.response.*;
import com.dan_michael.example.demo.model.entities.SubEn.*;
import com.dan_michael.example.demo.model.entities.img.ProductImg;
import com.dan_michael.example.demo.repositories.*;
import com.dan_michael.example.demo.repositories.image.ProductImgRepository;
import com.dan_michael.example.demo.repositories.SupRe.*;
import com.dan_michael.example.demo.util.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

import static com.dan_michael.example.demo.chatbot.service.ChatbotService.removeDiacritics;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final CommentRepository commentRepository;

    private final ProductImgRepository productImgRepository;

    private final QuantityDetailRepository quantityDetailRepository;

    private final DetailSizeQuantityRepository detailSizeQuantityRepository;

    private final TradeMarkRepository tradeMarkRepository;

    private final FavouriteProductRepository favouriteProductRepository;

    private final QuestionAnswerRepository questionAnswerRepository;

    private final OriginalQuestionRepository originalQuestionRepository;

    private final StyleRepository styleRepository;

    private final MaterialRepository materialRepository;

//-------------------Answer AI -----------------------------------
    public String saveInfoChatBotAnswer(ProductResponse qa,List<String> sizes,List<String> colors,List<SubColor> boxResponse){
        String save = "";
        String saveSizes = "";
        String saveColors = "";
        for (var x =0 ;x < sizes.size() ; ++x) {
            if(x < sizes.size()){
                saveSizes +=  sizes.get(x) + ", ";
            } else if (x == sizes.size()) {
                saveSizes +=  sizes.get(x);
            }
        }
        for (var x =0 ;x < colors.size() ; ++x) {
            if(x < colors.size()){
                saveColors +=  colors.get(x) + ", ";
            } else if (x == colors.size()) {
                saveColors +=  colors.get(x);
            }
        }
        String saveDetail = "";
        for (var x: boxResponse){
            saveDetail +=  "sản phẩm màu " + x.getColor().toUpperCase();
            for(var y: x.getSizes()){
                saveDetail += " có số lượng "+ y.quantity+ " cho kích thước "+ y.size.toUpperCase() +", ";
            }
            saveDetail += "Tiếp với ";
        }
        save =  "\n                                GIỚI THIỆU SẢN PHẨM                                \n" +
                "\n     " +
                "Chúng tôi xin giới thiệu sản phẩm " + qa.getName().toUpperCase() + " một lựa chọn tuyệt vời trong danh mục " + qa.getCategory().toUpperCase() + " và phân loại phụ " + qa.getSubCategory().toUpperCase() +
                "\n     " +
                "Sản phẩm này có màu sắc đa dạng, bao gồm "+saveColors.toUpperCase()+" cùng với các kích thước từ "+saveSizes.toUpperCase()+"." +
                "\n     " +
                "Với chi tiết số lượng phong phú, "+saveDetail+" Tổng số lượng sản phẩm hiện có là "+qa.getTotalQuantity()+"." +
                "\n     " +
                "Sản phẩm này có giá gốc "+String.valueOf(qa.getOriginalPrice()).toUpperCase()+" VND, hiện đang được giảm giá "+String.valueOf(qa.getSaleDiscountPercent()).toUpperCase()
                +"%, với giá cuối cùng là "+String.valueOf(qa.getFinalPrice()).toUpperCase()+" VND. Được đánh giá "+String.valueOf(qa.getRating()).toUpperCase()+" ✨, "+ qa.getName() +" không chỉ có chất lượng xuất sắc mà còn mang đến giá trị tốt cho khách hàng." +
                "\n     " +
                "Tình trạng của sản phẩm hiện tại là có khuyến mãi và mới. Nhận xét từ các khách hàng cho biết sản phẩm mang lại giá trị rất tốt với chất lượng tuyệt vời." +
                "\n     " +
                "Hy vọng mẫu giới thiệu này đáp ứng được yêu cầu của bạn!";
        return save;
    }
    public String savesizesAnswer(String namepro,List<String> sizes){
        String save = "";
        String saveSizes = "";
        for (var x =0 ;x < sizes.size() ; ++x) {
            if(x < sizes.size()){
                saveSizes +=  sizes.get(x) + ", ";
            } else if (x == sizes.size()) {
                saveSizes +=  sizes.get(x);
            }
        }
        return save + " Hiện Tại có " + sizes.size() + " kích thước .Danh sách gồm : [" +saveSizes+"]. Để biết thông tin chi biết về số lương và kích thước chi tiết bạn có thể đặt câu hỏi : Chi tiết Quantity Detail of "+namepro;
    }
    public String savecolorsAnswer(String namepro,List<String> colors){
        String save = namepro ;
        String savecolors = "";
        for (var x =0 ;x < colors.size() ; ++x) {
            if(x < colors.size()){
                savecolors +=  colors.get(x) + ", ";
            } else if (x == colors.size()) {
                savecolors +=  colors.get(x);
            }
        }
        return save + " Hiện Tại có " + colors.size() + " màu .Danh sách gồm : [" +savecolors+"]. Để biết thông tin chi biết về số lương và màu chi tiết bạn có thể đặt câu hỏi : Chi tiết Quantity Detail of "+namepro;
    }
    public String saveQuantityDetailAnswer(ProductResponse qa,List<String> sizes,List<String> colors,List<SubColor> boxResponse){
        String saveDetail = "Chi tiết Quantity Detail of " + qa.getName() + "\n";
        for (var x: boxResponse){
            saveDetail +=  "Màu " + x.getColor().toUpperCase();
            for(var y: x.getSizes()){
                saveDetail += " có số lượng "+ y.quantity+ " cho kích thước "+ y.size.toUpperCase() +".\n";
            }
        }
        return saveDetail + "Nếu bạn muốn yêu cầu màu hoặc thích thước khác thì bạn có thể gửi cầu hỏi cho quản trị viên để có thể nhận được phản hồi sớm !";
    }

    public String rating(String namepro,Float rating, Integer nRating){
        if(rating >= 4){
            return "Sản phẩm "+ namepro + " hiện đang được đánh giá tôt nhất với rating là " + rating +" ✨ với tổng "+ nRating+ " đánh giá từ khách hàng. Tôi nghĩ sản phẩm này là 1 trong những lựa chọn tốt cho bạn";
        }else if(rating >= 3){
            return "Sản phẩm "+ namepro + " hiện đang được đánh giá ổn với rating là " + rating +" \uD83C\uDF1F với tổng "+ nRating+ " đánh giá từ khách hàng. Đây có thể sẽ là sản phẩm phù với bạn hoặc bạn có thể tìm kiếm các sản phẩm tương tự tốt hơn";
        }else {
            return "Sản phẩm" + namepro + " hiện đang được đánh giá không được tốt với rating là " + rating +" ⭐ với tổng "+ nRating+" đánh giá từ khách hàng. Sản phẩm đang được hoàn thiện và cải tiến để có thể đáp ứng tốt hơn khách hang \n.Nếu bạn thấy sản phẩm phù hợp với bản thân thi có thể thêm vào giở hàng và tiến hành order";
        }
    }
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
//                SubImgResponse response = SubImgResponse.builder()
//                        .id(productImg.getId())
//                        .img_url(productImg.getImg_url())
//                        .imageName(productImg.getImageName())
//                        .identification(productImg.getIdentification())
//                        .build();
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
        var oriqe0 = product_flag.getName();
        var oriqe1 = ("Bạn có sản phẩm "+ save.getName()+" không?");
        var oriqe2 = ("Giá của "+ save.getName()+" là bao nhiêu?");
        var oriqe3 = ("Bạn có chương trình khuyến mãi nào cho "+ save.getName()+" không?");
        var oriqe4 = ("Sản phẩm "+ save.getName()+" có còn hàng không?");
        var oriqe5 = ("Tôi có thể xem đánh giá của khách hàng về "+ save.getName()+" ở đâu?");
        var oriqe6 = ("Tôi có thể trả lại "+ save.getName()+" nếu không hài lòng không?");
//        var oriqe7 = ("Tôi có thể thay đổi hoặc hủy đơn hàng của mình không?");
        var oriqe8 = (save.getName()+" hiện có màu sắc nào");
        var oriqe9 = (save.getName()+" hiện có kích thước nào");
        var oriqe10 = ("Chi tiết Quantity Detail of "+ save.getName());
        var oriqe11 = (save.getName()+" thuộc hãng nào?");
        var oriqe13 = ("Giá gốc của "+ save.getName()+" là bao nhiêu?");
        var oriqe14 = ("Giá khuyến mãi hiện tại của "+ save.getName()+" là bao nhiêu?");
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe0).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe1).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe2).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe3).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe4).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe5).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe6).build());
//        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe7).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe8).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe9).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe10).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe11).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe13).build());
        originalQuestionRepository.save(OriginalQuestion.builder().question(oriqe14).build());


        //info
        QuestionAnswer qa0 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe0.toLowerCase()))
                .answer(saveInfoChatBotAnswer(save,sizes,colors,BoxResponse))
                .build();
        //Bạn có sản phẩm [tên sản phẩm] không?
        QuestionAnswer qa1 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe1.toLowerCase()))
                .answer("Vâng, chúng tôi có "+ save.getName()+". Bạn có thể tìm thấy nó trong danh mục sản phẩm hoặc nhấn vào liên kết sản phẩm.")
                .build();
        //Giá của [tên sản phẩm] là bao nhiêu?
        QuestionAnswer qa2 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe2.toLowerCase()))
                .answer("")
                .build();
        //Bạn có chương trình khuyến mãi nào cho [tên sản phẩm] không?
        QuestionAnswer qa3 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe3.toLowerCase()))
                .answer("Hiện tại, chúng tôi đang có chương trình khuyến mãi "+ save.getSaleDiscountPercent()+"% cho "+ save.getName()+". Bạn có thể tìm hiểu thêm tại trang khuyến mãi của chúng tôi.")
                .build();
        //Sản phẩm [tên sản phẩm] có còn hàng không?
        QuestionAnswer qa4 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe4.toLowerCase()))
                .answer("Tổng hàng tồn kho của "+ save.getName()+" còn "+save.getTotalQuantity()+" sản phẩm các loại, Để kiểm tra tình trạng hàng tồn kho chi tiết, Bạn vui lòng nhấn vào trang sản phẩm của chúng tôi. ")
                .build();
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
//        //Tôi có thể thay đổi hoặc hủy đơn hàng của mình không?
//        QuestionAnswer qa7 = QuestionAnswer.builder()
//                .question(removeDiacritics(oriqe7.toLowerCase()))
//                .answer("Hiện tại, chúng tôi đang có chương trình khuyến mãi "+ save.getSaleDiscountPercent()+"% cho "+ save.getName()+". Bạn có thể tìm hiểu thêm tại trang khuyến mãi của chúng tôi.")
//                .build();
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
        // [tên sản phẩm] thuộc hãng nào?
        QuestionAnswer qa11 = QuestionAnswer.builder()
                .question(removeDiacritics(oriqe11.toLowerCase()))
                .answer((save.getName() + " thuộc hãng " + save.getTradeMask() + " ." + save.getTradeMask()+ " là 1 nhãn hàng với chất liệu khá tốt, sản phẩm đẹp mắt và hiện đâng hợp tác với chúng tôi"))
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
        questionAnswerRepository.save(qa0);
        questionAnswerRepository.save(qa1);
        questionAnswerRepository.save(qa2);
        questionAnswerRepository.save(qa3);
        questionAnswerRepository.save(qa4);
        questionAnswerRepository.save(qa5);
        questionAnswerRepository.save(qa6);
//        questionAnswerRepository.save(qa7);
        questionAnswerRepository.save(qa8);
        questionAnswerRepository.save(qa9);
        questionAnswerRepository.save(qa10);
        questionAnswerRepository.save(qa11);
        //12 ở chỗ comment
        questionAnswerRepository.save(qa13);
        questionAnswerRepository.save(qa14);
//        questionAnswerRepository.save(qa15);
//        questionAnswerRepository.save(qa16);
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

        var product_flag = productRepository.findByName_(request.getName());

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

//            System.out.println("Colors : " + colors);
//            System.out.println("sizes : " + sizes);
//
//            System.out.println("Color_add : " + colorsadd);
//            System.out.println("size_add : " + sizesadd);
//
//            System.out.println("Color_change : " + colorchange);
//            System.out.println("size_change : " + sizechange);
//
//            System.out.println("xoaColor : " + xoaColor);
//            System.out.println("xoaSize : " + xoaSize);
//
//            System.out.println("addColor : " + addColor);
//            System.out.println("addSize : " + addSize);
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
//                            SubImgResponse response = SubImgResponse.builder()
//                                    .id(productImagesBox_0.get(y).getId())
//                                    .img_url(productImagesBox_0.get(y).getImg_url())
//                                    .imageName(productImagesBox_0.get(y).getImageName())
//                                    .identification(productImagesBox_0.get(y).getIdentification())
//                                    .build();
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
//        QuestionAnswer qa = questionAnswerRepository.findByQuestion(product_flag.getSkuQa().toLowerCase());
//        if(qa !=null){
//            qa.setAnswer(saveInfoChatBotAnswer(save,valuesave(sizesadd,sizes),valuesave(colorsadd,colors),BoxResponse));
//        }
//        questionAnswerRepository.save(qa);
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
            Boolean isBestSelling
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
        // Implementing Pagination
        if(_page == null){
            _page = 1;
        }
        if(_limit == null){
            _limit = 20;
        }
        int start = Math.max((_page - 1) * _limit, 0);
        int end = Math.min(start + _limit, productList.size());
        List<Product> paginatedProducts = productList.subList(start, end);

        List<ProductResponse> productsResponseList = new ArrayList<>();
        for (var x : paginatedProducts) {
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
        return ProductListDtos.builder().data(productsResponseList).paginationDto(new PaginationDto(_limit,_page,_total)).build();
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
        var status = true;
        if(commentDto.getContent() == null ){
            status = false;
        }
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .rating(commentDto.getRating())
                .color(commentDto.getColor())
                .size(commentDto.getSize())
                .image(product.getImageMain())
//                .productQuality(commentDto.getProductQuality())
                .identification_pro(product.getName())
                .rate_status(status)
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
        if(questionAnswerRepository.findByQuestion("chất lượng của "+ product.getName()+" được đánh giá thế nào?") !=null){
            qa12 = questionAnswerRepository.findByQuestion("chất lượng của "+ product.getName()+" được đánh giá thế nào?");
            qa12.setAnswer(rating(product.getName(),rating,nRating));
        }else{
            qa12 = QuestionAnswer.builder()
                    .question(removeDiacritics(("chất lượng của "+ product.getName()+" được đánh giá thế nào?").toLowerCase()))
                    .answer(rating(product.getName(),rating,nRating))
                    .build();
        }
        originalQuestionRepository.save(OriginalQuestion.builder().question("chất lượng của "+ product.getName()+" được đánh giá thế nào?").build());
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
        comment.setContent(commentDto.getContent());
        comment.setRating(commentDto.getRating());
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
        public List<TradeMark> findAll() {
            return tradeMarkRepository.findAll();
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
}
