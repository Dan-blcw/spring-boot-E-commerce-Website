package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.sub.SubBrandsResponse;
import com.dan_michael.example.demo.model.dto.ob.sub.SubQuantityResponse;
import com.dan_michael.example.demo.model.entities.SubEn.*;
import com.dan_michael.example.demo.model.response.ProductImgResponse;
import com.dan_michael.example.demo.model.response.ProductResponse;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CommentDto;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
import com.dan_michael.example.demo.model.dto.ob.Test;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.repositories.CommentRepository;
import com.dan_michael.example.demo.repositories.ProductImgRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
import com.dan_michael.example.demo.repositories.SupRe.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

        product_flag.setRating(5.0f);
        product_flag.setNRating(0);


        product_flag.setOriginalPrice(request.getOriginalPrice());
        product_flag.setSaleDiscountPercent(request.getSaleDiscountPercent());
        var finalPrice = request.getOriginalPrice() - request.getOriginalPrice()* (request.getSaleDiscountPercent()/100);
        product_flag.setFinalPrice(finalPrice);
        product_flag.setSaleStatus(request.getSaleStatus());
        product_flag.setBrand(request.getBrand());
        product_flag.setNewStatus(true);
        product_flag.setFavourite(null);

        product_flag.setCreateDate(new Date());
        product_flag.setCreatedByUserid(request.getCreatedByUserid());


        List<QuantityDetail> Box = new ArrayList<>();
        if (request.getQuantityDetail() != null && request.getQuantityDetail().size() > 0) {
            for (var x : request.getQuantityDetail()) {
                QuantityDetail Item = new QuantityDetail();
                Item.setColor(x.getColor());
                Item.setSize(x.getSize());
                Item.setQuantity(x.getQuantity());
                Item.setIdentification(product_flag.getName());
                totalQuantity += x.getQuantity();
                if(!sizes.contains(x.getSize())){
                    sizes.add(x.getSize());
                }
                if(!colors.contains(x.getColor())){
                    colors.add(x.getColor());
                }
                Box.add(Item);
                quantityDetailRepository.save(Item);
            }
        }


        List<ProductImgResponse> productImagesBox = new ArrayList<>();
        List<ProductImg> productImagesBox_0 = new ArrayList<>();
        if (request.getImages() != null && request.getImages().size() > 0) {
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
                        .path("/api/v1/global/media/images/")
                        .path(imageFile.getOriginalFilename())
                        .toUriString());
                productImgRepository.save(productImg);
                ProductImgResponse response = ProductImgResponse.builder()
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
        var productResponse = ProductResponse.builder()
                .id(product_flag.getId())
                .images(productImagesBox)
                .colours(colors)
                .sizes(sizes)
                .brand(request.getBrand())
                .name(product_flag.getName())
                .description(product_flag.getDescription())
                .quantity(product_flag.getQuantityDetails())
                .category(product_flag.getCategory())
                .rating(product_flag.getRating())
                .nRating(product_flag.getNRating())
                .totalQuantity(totalQuantity)
                .favourite(null)

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

    public ProductResponse updateProduct(ProductDtos request) {
        var totalQuantity = 0;
        List<String> sizes = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        var product_flag = productRepository.findByName_(request.getName());

        if(product_flag != null){
            product_flag.setName(request.getName());
            product_flag.setDescription(request.getDescription());

            product_flag.setCategory(request.getCategory());
            product_flag.setBrand(request.getBrand());
            product_flag.setNRating(0);

            product_flag.setOriginalPrice(request.getOriginalPrice());
            product_flag.setSaleDiscountPercent(request.getSaleDiscountPercent());
            var finalPrice = request.getOriginalPrice() - request.getOriginalPrice()* (request.getSaleDiscountPercent()/100);
            product_flag.setFinalPrice(finalPrice);
            product_flag.setSaleStatus(request.getSaleStatus());

            product_flag.setNewStatus(true);

            product_flag.setCreateDate(new Date());
            product_flag.setCreatedByUserid(request.getCreatedByUserid());
        }

        List<QuantityDetail> Box = new ArrayList<>();
        if (request.getQuantityDetail() != null && request.getQuantityDetail().size() > 0) {
            for (var x : request.getQuantityDetail()) {
                QuantityDetail Item = new QuantityDetail();
                Item.setColor(x.getColor());
                Item.setSize(x.getSize());
                Item.setQuantity(x.getQuantity());
                Item.setIdentification(product_flag.getName());
                totalQuantity += x.getQuantity();
                if(!sizes.contains(x.getSize())){
                    sizes.add(x.getSize());
                }
                if(!colors.contains(x.getColor())){
                    colors.add(x.getColor());
                }
                Box.add(Item);
                quantityDetailRepository.save(Item);
            }
        }
        List<ProductImgResponse> productImagesBox = new ArrayList<>();
        List<ProductImg> productImagesBox_0 = new ArrayList<>();
        if (request.getImages() != null && request.getImages().size() > 0) {
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
                        .path("/api/v1/global/media/images/")
                        .path(imageFile.getOriginalFilename())
                        .toUriString());
                productImgRepository.save(productImg);
                ProductImgResponse response = ProductImgResponse.builder()
                        .id(productImg.getId())
                        .img_url(productImg.getImg_url())
                        .imageName(productImg.getImageName())
                        .identification(productImg.getIdentification())
                        .build();
                productImagesBox.add(response);
                productImagesBox_0.add(productImg);
            }
        }


        product_flag.setImages(productImagesBox_0);
        product_flag.setTotalQuantity(totalQuantity);
        product_flag.setQuantityDetails(Box);
        List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIAndIdentification(product_flag.getName());
        List<Integer> favouriteListRe = new ArrayList<>();
        for (var x_0: favouriteList) { favouriteListRe.add(x_0.getUser_id());}

        productRepository.save(product_flag);
        var productResponse = ProductResponse.builder()
                .id(product_flag.getId())
                .images(productImagesBox)
                .brand(request.getBrand())
                .sizes(sizes)
                .colours(colors)
                .totalQuantity(totalQuantity)
                .name(product_flag.getName())
                .description(product_flag.getDescription())
                .quantity(product_flag.getQuantityDetails())
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

    public void removeImageFromProduct(Integer productId, Integer imageId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.getImages().removeIf(image -> image.getId().equals(imageId));
            productRepository.save(product);
        }
    }

    public List<ProductResponse> findAllHander() {
        List<ProductResponse> productsResponseList = new ArrayList<>();
        var flag = productRepository.findAll();
        flag.sort(Comparator.comparing(Product::getFinalPrice).reversed());
        for (var x : flag) {
            List<ProductImg> imgs = productImgRepository.findProductImgByProductId(x.getName());
            List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(x.getName());
            List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(x.getName());
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIAndIdentification(x.getName());

            List<ProductImgResponse> productImagesBox = new ArrayList<>();
            List<Integer> favouriteListRe = new ArrayList<>();
            List<String> colorsListRe = new ArrayList<>();
            List<String> sizesListRe = new ArrayList<>();
            Float rating = 0.0f;
            int nRating = commentsList.size();
            for (var x_0: commentsList) {
                rating += x_0.getRating();
            }
            rating = rating/nRating;
            for (var x_0: favouriteList) { favouriteListRe.add(x_0.getUser_id());}
            for (var x_0: quantityDetailsList) {
                if(!colorsListRe.contains(x_0.getColor())){
                    colorsListRe.add(x_0.getColor());
                }
                if(!sizesListRe.contains(x_0.getSize())){
                    sizesListRe.add(x_0.getSize());
                }
            }
            for (var x_0: imgs) {
                ProductImgResponse response = ProductImgResponse.builder()
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
                    .brand(x.getBrand())
                    .sizes(sizesListRe)
                    .colours(colorsListRe)
                    .totalQuantity(x.getTotalQuantity())
                    .description(x.getDescription())
                    .quantity(quantityDetailsList)
                    .category(x.getCategory())
                    .rating(rating)
                    .nRating(nRating)
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
    public ProductResponse findbyIDHander(Integer id){
        var boxItem = productRepository.findById(id);
        List<ProductImg> imgs = productImgRepository.findProductImgByProductId(boxItem.get().getName());
        List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(boxItem.get().getName());
        List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(boxItem.get().getName());
        List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIAndIdentification(boxItem.get().getName());

        List<ProductImgResponse> productImagesBox = new ArrayList<>();
        List<String> colorsListRe = new ArrayList<>();
        List<String> sizesListRe = new ArrayList<>();
        for (var x_0: quantityDetailsList) {
            if(!colorsListRe.contains(x_0.getColor())){
                colorsListRe.add(x_0.getColor());
            }
            if(!sizesListRe.contains(x_0.getSize())){
                sizesListRe.add(x_0.getSize());
            }
        }
        List<Integer> favouriteListRe = new ArrayList<>();
        Float rating = 0.0f;
        int nRating = commentsList.size();
        for (var x_0: commentsList) {
            rating += x_0.getRating();
        }
        rating = rating/nRating;
        for (var x_0: favouriteList) { favouriteListRe.add(x_0.getUser_id());}
        for (var x_0: imgs) {
            ProductImgResponse response = ProductImgResponse.builder()
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
                .sizes(sizesListRe)
                .colours(colorsListRe)
                .brand(boxItem.get().getBrand())

                .name(boxItem.get().getName())
                .description(boxItem.get().getDescription())
                .quantity(quantityDetailsList)
                .category(boxItem.get().getCategory())
                .rating(rating)
                .nRating(nRating)
                .favourite(favouriteListRe)
                .totalQuantity(boxItem.get().getTotalQuantity())
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
        productRepository.deleteById(id);
        return ResponseMessageDtos.builder()
                .status(200)
                .message("Delete Product successfully !")
                .build();
    }
//-------------------comments-----------------------------------
    public List<Comment> listCommentByIdentification_pro (String identification) throws ChangeSetPersister.NotFoundException {

        List<Comment> commentList =  commentRepository.findCommentByIAndIdentification_pro(identification);
        return commentList;
    }

    public List<Comment> listCommentByIdentification_user (String identification) throws ChangeSetPersister.NotFoundException {

        List<Comment> commentList =  commentRepository.findCommentByIAndIdentification_user(identification);
        return commentList;
    }


    public List<Comment> listComment () throws ChangeSetPersister.NotFoundException {

        List<Comment> commentList =  commentRepository.findAll();
        return commentList;
    }

    public Comment createComment (CommentDto commentDto,Integer product_id) throws ChangeSetPersister.NotFoundException {

        Product product = productRepository.findById(product_id).orElseThrow(()-> new ChangeSetPersister.NotFoundException());
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

    public ResponseMessageDtos updateComment (CommentDto commentDto,Integer comment_id) throws ChangeSetPersister.NotFoundException {
        Comment comment = commentRepository.findCommentById(comment_id);
        List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(comment.getIdentification_pro());
        Product product = productRepository.findByName_(comment.getIdentification_pro());

        if(comment == null){
            return ResponseMessageDtos.builder().status(400).message("Error! Comment Do not exits").build();
        }
        if(!Objects.equals(comment.getIdentification_user(), commentDto.getUsername())){
            return ResponseMessageDtos.builder().status(400).message("Error! You are not the one commenting on this comment, Editing failed !!!").build();
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
        return ResponseMessageDtos.builder().status(200).message("Update Comment successfully !").build();
    }

    public CommentDto deleteCommentDto (Integer comment_id) throws ChangeSetPersister.NotFoundException {

        Comment comment = commentRepository.findById(comment_id).orElseThrow(()-> new ChangeSetPersister.NotFoundException());
        commentRepository.delete(comment);
        return null;
    }
//-------------------------------------------Favourite-----------------------------------------------------------------
    public List<ProductResponse> findbyFavouriteByUserID(Integer id){
        List<ProductResponse> boxProduct = new ArrayList<>();
        var boxItems = productRepository.findAll();
        for (var x : boxItems) {
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIAndIdentification(x.getName());
            List<Integer> favouriteListRe = new ArrayList<>();
            for (var x_0: favouriteList) { favouriteListRe.add(x_0.getUser_id());}
            if(favouriteListRe.contains(id)){
                List<ProductImg> imgs = productImgRepository.findProductImgByProductId(x.getName());
                List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(x.getName());
                List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(x.getName());

                List<ProductImgResponse> productImagesBox = new ArrayList<>();
                List<String> colorsListRe = new ArrayList<>();
                List<String> sizesListRe = new ArrayList<>();
                for (var x_0: quantityDetailsList) {
                    if(!colorsListRe.contains(x_0.getColor())){
                        colorsListRe.add(x_0.getColor());
                    }
                    if(!sizesListRe.contains(x_0.getSize())){
                        sizesListRe.add(x_0.getSize());
                    }
                }
                Float rating = 0.0f;
                int nRating = commentsList.size();
                for (var x_0: commentsList) {
                    rating += x_0.getRating();
                }
                rating = rating/nRating;
                for (var x_0: imgs) {
                    ProductImgResponse response = ProductImgResponse.builder()
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
                        .brand(x.getBrand())
                        .name(x.getName())
                        .description(x.getDescription())
                        .quantity(quantityDetailsList)
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
    public String addFavourite(String product_name,Integer user_id) {

        var product_flag = productRepository.findByName_(product_name);
        if(product_flag != null){
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIAndIdentification(product_name);
            FavouriteProduct newF = FavouriteProduct.builder()
                    .user_id(user_id)
                    .identification(product_name)
                    .build();
            favouriteList.add(newF);
            favouriteProductRepository.save(newF);
            product_flag.setFavourite(favouriteList);
            productRepository.save(product_flag);
            return  "Add Favorite successfully !!!";
        }
        return "Add Favorite Failure !!!";
    }

    public String deleteFavourite(String product_name,Integer user_id) {

        var product_flag = productRepository.findByName_(product_name);
        if(product_flag != null){
            favouriteProductRepository.deleteByUser_id(user_id,product_name);
            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIAndIdentification(product_name);
            product_flag.setFavourite(favouriteList);
            productRepository.save(product_flag);
            return  "delete Favorite successfully !!!";
        }
        return "delete Favorite Failure !!!";
    }
//--------------------------------------Search--------------------------------------------------------------
    public List<ProductResponse> search_all(
            List<String> brands,
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
        for (var x : brands) {
            List<Product> box = productRepository.search_all(x, isPromotion, isReleased, ratingGte,ratingLt, priceGte, priceLte);
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
            List<ProductImg> imgs = productImgRepository.findProductImgByProductId(x.getName());
            List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(x.getName());
            List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(x.getName());

            List<FavouriteProduct> favouriteList = favouriteProductRepository.findFavouriteByIAndIdentification(x.getName());

            List<ProductImgResponse> productImagesBox = new ArrayList<>();
            List<Integer> favouriteListRe = new ArrayList<>();
            List<String> colorsListRe = new ArrayList<>();
            List<String> sizeListRe = new ArrayList<>();
            for (var x_0: quantityDetailsList) {
                if(!colorsListRe.contains(x_0.getColor())){
                    colorsListRe.add(x_0.getColor());
                }
                if(!sizeListRe.contains(x_0.getSize())){
                    sizeListRe.add(x_0.getSize());
                }
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
                ProductImgResponse response = ProductImgResponse.builder()
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
                    .brand(x.getBrand())
                    .sizes(sizeListRe)
                    .colours(colorsListRe)
                    .name(x.getName())
                    .description(x.getDescription())

                    .quantity(quantityDetailsList)
                    .totalQuantity(x.getTotalQuantity())

                    .category(x.getCategory())
                    .rating(rating)
                    .nRating(nRating)

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

    public SubQuantityResponse getQuantityByColorAndSize(Integer id,String color,String size){
        var boxItem = productRepository.findById(id);
        List<QuantityDetail> quantityDetailsList = quantityDetailRepository.findQuantityDetailsByIAndIdentification(boxItem.get().getName());
        for (var x_0: quantityDetailsList) {
            if(x_0.getColor().toLowerCase().equals(color.toLowerCase()) && x_0.getSize().toLowerCase().equals(size.toLowerCase())){
                var flag =  SubQuantityResponse.builder()
                        .quantity(x_0.getQuantity())
                        .message("get Quantity Successfully")
                        .build();
                System.out.println(x_0.getQuantity());
                return flag;
            }
        }
        return SubQuantityResponse.builder()
                .quantity(-1)
                .message("ERROR to get Quantity bcs Color or Size is wrong")
                .build();
    }

    public SubBrandsResponse getbrands(){
        List<String> boxBrands = new ArrayList<>();
        var boxItem = productRepository.findAll();
        for (var x_0: boxItem) {
            if(!boxBrands.contains(x_0.getBrand())){
                boxBrands.add(x_0.getBrand());
            }

        }
        return SubBrandsResponse.builder()
                .brands(boxBrands)
                .message("ERROR to get Quantity bcs Color or Size is wrong")
                .build();
    }
}
