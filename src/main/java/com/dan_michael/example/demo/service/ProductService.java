package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.*;
import com.dan_michael.example.demo.model.dto.ob.sub.SubColor;
import com.dan_michael.example.demo.model.dto.ob.sub.SubSizeQuantity;
import com.dan_michael.example.demo.model.entities.TradeMark;
import com.dan_michael.example.demo.model.response.*;
import com.dan_michael.example.demo.model.entities.SubEn.*;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.img.ProductImg;
import com.dan_michael.example.demo.repositories.CommentRepository;
import com.dan_michael.example.demo.repositories.TradeMarkRepository;
import com.dan_michael.example.demo.repositories.image.ProductImgRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
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

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CommentRepository commentRepository;

    private final ProductImgRepository productImgRepository;

    private final QuantityDetailRepository quantityDetailRepository;

    private final DetailSizeQuantityRepository detailSizeQuantityRepository;

    private final TradeMarkRepository tradeMarkRepository;

    private final FavouriteProductRepository favouriteProductRepository;
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
        product_flag.setName(request.getName());
        product_flag.setDescription(request.getDescription());

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
        }
        product_flag.setFinalPrice(finalPrice);
        product_flag.setNewStatus(true);
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


        List<SubImgResponse> productImagesBox = new ArrayList<>();
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
                if(productImg.getImageName() == request.getImageMain()){
                    product_flag.setImageMain(productImg.getImg_url());
                }
                SubImgResponse response = SubImgResponse.builder()
                        .id(productImg.getId())
                        .img_url(productImg.getImg_url())
                        .imageName(productImg.getImageName())
                        .identification(productImg.getIdentification())
                        .build();
                productImagesBox.add(response);
                productImagesBox_0.add(productImg);
            }
        }

        product_flag.setQuantityDetails(Box);
        product_flag.setTotalQuantity(totalQuantity);
        product_flag.setImages(productImagesBox_0);

        productRepository.save(product_flag);
        return ProductResponse.builder()
                .id(product_flag.getId())
                .images(productImagesBox)
                .colours(colors)
                .sizes(sizes)
                .tradaMask(request.getTradeMask())
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
                .tradaMask(product_flag.getTradeMask())
                .newStatus(product_flag.getNewStatus())
                .comments(product_flag.getComments())
                .createDate(product_flag.getCreateDate())
                .createdByUserid(product_flag.getCreatedByUserid())
                .build();
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
            product_flag.setSubCategory(request.getSubCategory());
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
            }
            product_flag.setFinalPrice(finalPrice);

            product_flag.setNewStatus(false);
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

            System.out.println("Colors : " + colors);
            System.out.println("sizes : " + sizes);

            System.out.println("Color_add : " + colorsadd);
            System.out.println("size_add : " + sizesadd);

            System.out.println("Color_change : " + colorchange);
            System.out.println("size_change : " + sizechange);

            System.out.println("xoaColor : " + xoaColor);
            System.out.println("xoaSize : " + xoaSize);

            System.out.println("addColor : " + addColor);
            System.out.println("addSize : " + addSize);
            //      Xóa đi nếu có
            if ((xoaColor || xoaSize)) {
                if (request.getQuantityDetails() != null && !request.getQuantityDetails().isEmpty()) {
                    for (var x : quantityDetailsList) {
                        var detailSizeQuantitys = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x.getColor(),x.getIdentification());
                        if (colorchange.contains(x.getColor()) && xoaColor) {
                            quantityDetailRepository.deleteByIdentificationAndColor(product_flag.getName(), x.getColor());
                            for (var y : detailSizeQuantitys) {
                                detailSizeQuantityRepository.deleteByIdentificationAndSizeName(x.getColor(), y.getSize());
                            }
                        }
                        for (var y : detailSizeQuantitys) {
                            if (sizechange.contains(y.getSize()) && xoaSize) {
                                detailSizeQuantityRepository.deleteByIdentificationAndSizeName(x.getColor(), y.getSize());
                                continue;
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
                                var check = detailSizeQuantityRepository.DetailByIdentificationAndSizeName(x.getColor(), y.getSize());
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
        List<SubImgResponse> productImagesBox = new ArrayList<>();
        List<String> boxSend = new ArrayList<>();
        List<String> boxSave = new ArrayList<>();
        List<String> boxOldImage = new ArrayList<>();
        List<ProductImg> productImagesBox_0 = productImgRepository.findProductImgByProductName(product_flag.getName());
        for (var x :productImagesBox_0){
            boxOldImage.add(x.getImageName());
        }
        if (request.getImages() != null && request.getImages().size() > 0) {
            for (MultipartFile imageFile : request.getImages()) {
                boxSave.add(imageFile.getOriginalFilename());
                ProductImg productImg = new ProductImg();
                if(boxOldImage.contains(imageFile.getOriginalFilename())){
                    var saveImg = productImgRepository.findProductImgByimageName_(imageFile.getOriginalFilename(),product_flag.getName());
                    SubImgResponse response = SubImgResponse.builder()
                            .id(saveImg.getId())
                            .img_url(saveImg.getImg_url())
                            .imageName(saveImg.getImageName())
                            .identification(saveImg.getIdentification())
                            .build();

                    if(!productImagesBox.contains(response)){
                        productImagesBox.add(response);
                    }
                    continue;
                }
                boxSend.add(imageFile.getOriginalFilename());
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
                if(productImg.getImageName() == product_flag.getImageMain() || product_flag.getImageMain() != null){
                    product_flag.setImageMain(productImg.getImg_url());
                }
                SubImgResponse response = SubImgResponse.builder()
                        .id(productImg.getId())
                        .img_url(productImg.getImg_url())
                        .imageName(productImg.getImageName())
                        .identification(productImg.getIdentification())
                        .build();
                productImagesBox.add(response);
                productImagesBox_0.add(productImg);
            }
        }
//        Xóa Đi Ảnh Cũ nếu có
        List<String> boxNewSave = valueadd(boxSave,boxSend);
        for(var x : valueadd(boxNewSave,boxOldImage)){
            productImgRepository.deleteByIdentificationAndImageName(product_flag.getName(),x);
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
        var productResponse = ProductResponse.builder()
                .id(product_flag.getId())
                .images(productImagesBox)
                .subCategory(request.getSubCategory())
                .sizes(valuesave(sizesadd,sizes))
                .colours(valuesave(colorsadd,colors))
                .totalQuantity(totalQuantity)
                .imageMain(product_flag.getImageMain())
                .name(product_flag.getName())
                .tradaMask(product_flag.getTradeMask())
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
        return productResponse;
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

//    public void removeImageFromProduct(Integer productId, Integer imageId) {
//        Product product = productRepository.findById(productId).orElse(null);
//        if (product != null) {
//            product.getImages().removeIf(image -> image.getId().equals(imageId));
//            productRepository.save(product);
//        }
//    }

    public List<ProductResponse> findAllHander() {
        List<ProductResponse> productsResponseList = new ArrayList<>();
        var flag = productRepository.findAll();
        flag.sort(Comparator.comparing(Product::getFinalPrice).reversed());
        for (var x : flag) {
            List<ProductImg> imgs = productImgRepository.findProductImgByProductName(x.getName());
            List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(x.getName());
            List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(x.getName());
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIdentification(x.getName());

            List<SubImgResponse> productImagesBox = new ArrayList<>();
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
            for (var x_0: favouriteList) { favouriteListRe.add(x_0.getUser_id());}
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
                SubImgResponse response = SubImgResponse.builder()
                        .id(x_0.getId())
                        .img_url(x_0.getImg_url())
                        .imageName(x_0.getImageName())
                        .identification(x_0.getIdentification())
                        .build();
                productImagesBox.add(response);
            }
            var y = ProductResponse.builder()
                    .id(x.getId())
                    .images(productImagesBox)
                    .name(x.getName())
                    .sizes(sizesListRe)
                    .colours(colorsListRe)
                    .subCategory(x.getSubCategory())
                    .totalQuantity(x.getTotalQuantity())
                    .description(x.getDescription())
                    .quantityDetails(subColors)
                    .category(x.getCategory())
                    .rating(rating)
                    .nRating(nRating)
                    .favourite(favouriteListRe)
                    .tradaMask(x.getTradeMask())
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
        return productsResponseList;
    }
    public ProductResponse findbyIDHander(Integer id){
        var boxItem = productRepository.findById(id);
        List<ProductImg> imgs = productImgRepository.findProductImgByProductName(boxItem.get().getName());
        List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(boxItem.get().getName());
        List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(boxItem.get().getName());
        List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIdentification(boxItem.get().getName());

        List<SubImgResponse> productImagesBox = new ArrayList<>();
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
        for (var x_0: favouriteList) { favouriteListRe.add(x_0.getUser_id());}
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
            SubImgResponse response = SubImgResponse.builder()
                    .id(x_0.getId())
                    .img_url(x_0.getImg_url())
                    .imageName(x_0.getImageName())
                    .identification(x_0.getIdentification())
                    .build();
            productImagesBox.add(response);
        }
        var productResponse = ProductResponse.builder()
                .id(boxItem.get().getId())
                .images(productImagesBox)
                .name(boxItem.get().getName())
                .sizes(sizesListRe)
                .colours(colorsListRe)
                .subCategory(boxItem.get().getSubCategory())
                .totalQuantity(boxItem.get().getTotalQuantity())
                .description(boxItem.get().getDescription())
                .quantityDetails(subColors)
                .category(boxItem.get().getCategory())
                .rating(rating)
                .tradaMask(boxItem.get().getTradeMask())
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
    public Comment createComment (CommentDto commentDto,Integer product_id)
            throws ChangeSetPersister.NotFoundException
    {
        Product product = productRepository.findById(
                product_id).orElseThrow(()-> new ChangeSetPersister.NotFoundException());
        List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(product.getName());
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .rating(commentDto.getRating())
                .productQuality(commentDto.getProductQuality())
                .identification_pro(product.getName())
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
        comment.setProductQuality(commentDto.getProductQuality());
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

                List<SubImgResponse> productImagesBox = new ArrayList<>();
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
                    SubImgResponse response = SubImgResponse.builder()
                            .id(x_0.getId())
                            .img_url(x_0.getImg_url())
                            .imageName(x_0.getImageName())
                            .identification(x_0.getIdentification())
                            .build();
                    productImagesBox.add(response);
                }
                var productResponse = ProductResponse.builder()
                        .id(x.getId())
                        .images(productImagesBox)
                        .sizes(sizesListRe)
                        .colours(colorsListRe)
                        .subCategory(x.getSubCategory())
                        .name(x.getName())
                        .tradaMask(x.getTradeMask())
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
//--------------------------------------Search--------------------------------------------------------------
    public List<ProductResponse> search_all(
            String categoryName,
            List<String> subCategoryName,
            Boolean isPromotion,
            Boolean isReleased,
            Integer ratingGte,
            Integer priceGte,
            Integer priceLte,
            String sort) {
        var ratingLt = 6;
        if(ratingGte !=null){
            ratingLt = ratingGte +  1;
        }
        List<Product> productList = new ArrayList<>();
        for (var x : subCategoryName) {
            List<Product> box = productRepository.search_all(categoryName,x, isPromotion, isReleased, ratingGte,ratingLt, priceGte, priceLte);
            for (var y : box) {
                if (!productList.contains(y)) {
                    productList.add(y);
                }
            }
        }

        if ("ASC".equalsIgnoreCase(sort)) {
            productList.sort(Comparator.comparing(Product::getFinalPrice));
        } else if ("DESC".equalsIgnoreCase(sort)) {
            productList.sort(Comparator.comparing(Product::getFinalPrice).reversed());
        }
        List<ProductResponse> productsResponseList = new ArrayList<>();
        for (var x : productList) {
            List<ProductImg> imgs = productImgRepository.findProductImgByProductName(x.getName());
            List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(x.getName());
            List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(x.getName());
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIdentification(x.getName());

            List<SubImgResponse> productImagesBox = new ArrayList<>();
            List<Integer> favouriteListRe = new ArrayList<>();
            List<String> colorsListRe = new ArrayList<>();
            List<String> sizeListRe = new ArrayList<>();
            List<SubColor> subColors = new ArrayList<>();
            for (var x_0: quantityDetailsList) {
                List<DetailSizeQuantity> detailSizeQuantities = detailSizeQuantityRepository.findDetailSizeQuantityByIdentification(x_0.getColor(),x_0.getIdentification());
                if(!colorsListRe.contains(x_0.getColor())){
                    colorsListRe.add(x_0.getColor());
                }
                List<SubSizeQuantity> sizeQuantities = new ArrayList<>();
                for (var y_0: detailSizeQuantities){
                    if(!sizeListRe.contains(y_0.getSize())){
                        sizeListRe.add(y_0.getSize());
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
            for (var x_0 : commentsList) {
                rating += x_0.getRating();
            }
            rating = rating / nRating;
            for (var x_0 : favouriteList) {
                favouriteListRe.add(x_0.getUser_id());
            }
            for (var x_0: imgs) {
                SubImgResponse response = SubImgResponse.builder()
                        .id(x_0.getId())
                        .img_url(x_0.getImg_url())
                        .imageName(x_0.getImageName())
                        .identification(x_0.getIdentification())
                        .build();
                productImagesBox.add(response);
            }

            var y = ProductResponse.builder()
                    .id(x.getId())
                    .images(productImagesBox)
                    .subCategory(x.getSubCategory())
                    .sizes(sizeListRe)
                    .colours(colorsListRe)
                    .name(x.getName())
                    .description(x.getDescription())
                    .quantityDetails(subColors)
                    .totalQuantity(x.getTotalQuantity())
                    .category(x.getCategory())
                    .rating(rating)
                    .nRating(nRating)
                    .tradaMask(x.getTradeMask())
                    .tradaMask(x.getTradeMask())
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
        return productsResponseList;
    }

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
//    ----------------------------------TradeMask
        public List<TradeMark> findAll() {
            return tradeMarkRepository.findAll();
        }

        public Optional<TradeMark> findById(Integer id) {
            return tradeMarkRepository.findById(id);
        }

        public TradeMark saveTrask(TradeMaskDtos tradeMark) {
            var save = TradeMark.builder()
                    .tradeMarkSku(tradeMark.getTradeMarkSku())
                    .tradeMarkName(tradeMark.getTradeMarkName())
                    .description(tradeMark.getDescription())
                    .createDate(new Date())
                    .build();
            return tradeMarkRepository.save(save);
        }

        public void deleteById(Integer id) {
            tradeMarkRepository.deleteById(id);
        }


}
