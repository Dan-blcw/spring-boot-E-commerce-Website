package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.entities.SubEn.Brands;
import com.dan_michael.example.demo.model.entities.SubEn.Colors;
import com.dan_michael.example.demo.model.entities.SubEn.Sizes;
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
import com.dan_michael.example.demo.repositories.SupRe.BrandRepository;
import com.dan_michael.example.demo.repositories.SupRe.ColorsRepository;
import com.dan_michael.example.demo.repositories.SupRe.SizeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CommentRepository commentRepository;

    private final ProductImgRepository productImgRepository;

    private final ColorsRepository colorsRepository;

    private final BrandRepository brandRepository;

    private final SizeRepository sizeRepository;


//-------------------Product-----------------------------------
    public ProductResponse createProduct(ProductDtos request) {
        System.out.println(request.getColours());
        var ob = productRepository.findByName(request.getName());

        if(ob.isPresent()){
            return null;
        }
        var product_flag = new Product();
        product_flag.setName(request.getName());
        product_flag.setDescription(request.getDescription());
        product_flag.setPrice(request.getPrice());
        product_flag.setQuantity(request.getQuantity());
        product_flag.setCategory(request.getCategory());

        product_flag.setNRating(0);

        product_flag.setSaleStatus(request.getSaleStatus());
        product_flag.setSalePrice(request.getSalePrice());
        product_flag.setNewStatus(true);

        product_flag.setCreateDate(new Date());
        product_flag.setCreatedByUserid(request.getCreatedByUserid());

        List<Colors> colorsBox = new ArrayList<>();
        List<String> sendColors = new ArrayList<>();
        if (request.getColours() != null && request.getColours().size() > 0) {
            for (var x : request.getColours()) {
                Colors colorItem = new Colors();
                colorItem.setColor(x);
                colorItem.setIdentification(product_flag.getName());
                colorsBox.add(colorItem);
                colorsRepository.save(colorItem);
            }
        }

        List<Brands> brandBox = new ArrayList<>();
        if (request.getBrands() != null && request.getBrands().size() > 0) {
            for (var x : request.getColours()) {
                Brands brandItem = new Brands();
                brandItem.setBrand(x);
                brandItem.setIdentification(product_flag.getName());
                brandBox.add(brandItem);
                brandRepository.save(brandItem);
            }
        }

        List<Sizes> sizesBox = new ArrayList<>();
        if (request.getSizes() != null && request.getSizes().size() > 0) {
            for (var x : request.getColours()) {
                Sizes sizeItem = new Sizes();
                sizeItem.setSize(x);
                sizeItem.setIdentification(product_flag.getName());
                sizesBox.add(sizeItem);
                sizeRepository.save(sizeItem);
            }
        }

        List<ProductImg> productImagesBox = new ArrayList<>();
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
                productImagesBox.add(productImg);
                productImgRepository.save(productImg);
            }
        }

        product_flag.setColours(colorsBox);
        product_flag.setBrands(brandBox);
        product_flag.setSizes(sizesBox);
        product_flag.setImages(productImagesBox);

        productRepository.save(product_flag);
        var productResponse = ProductResponse.builder()
                .id(product_flag.getId())
                .images(productImagesBox)
                .colours(request.getColours())
                .sizes(request.getSizes())
                .brands(request.getBrands())
                .name(product_flag.getName())
                .description(product_flag.getDescription())
                .price(product_flag.getPrice())
                .quantity(product_flag.getQuantity())
                .category(product_flag.getCategory())
                .rating(product_flag.getRating())
                .nRating(product_flag.getNRating())
                .favourite(product_flag.getFavourite())
                .saleStatus(product_flag.getSaleStatus())
                .salePrice(product_flag.getSalePrice())
                .newStatus(product_flag.getNewStatus())
                .comments(product_flag.getComments())
                .createDate(product_flag.getCreateDate())
                .createdByUserid(product_flag.getCreatedByUserid())
                .build();
        return productResponse;
    }

    public Product test(Test request) {
        System.out.println(request.getName());
        System.out.println(request.getColours());
        var ob = productRepository.findByName(request.getName());

        if(ob.isPresent()){
            return null;
        }
        var product_flag = new Product();
        product_flag.setName(request.getName());
        product_flag.setNewStatus(true);
        product_flag.setCreateDate(new Date());
        List<Colors> colorsBox = new ArrayList<>();
        if (request.getColours() != null && request.getColours().size() > 0) {
            for (var x : request.getColours()) {
                Colors colorItem = new Colors();
                colorItem.setColor(x);
                colorItem.setIdentification(product_flag.getName());
                colorsBox.add(colorItem);
                colorsRepository.save(colorItem);
            }
        }

        List<Brands> brandBox = new ArrayList<>();
        if (request.getBrands() != null && request.getBrands().size() > 0) {
            for (var x : request.getColours()) {
                Brands brandItem = new Brands();
                brandItem.setBrand(x);
                brandItem.setIdentification(product_flag.getName());
                brandBox.add(brandItem);
                brandRepository.save(brandItem);
            }
        }

        List<Sizes> sizesBox = new ArrayList<>();
        if (request.getSizes() != null && request.getSizes().size() > 0) {
            for (var x : request.getColours()) {
                Sizes sizeItem = new Sizes();
                sizeItem.setSize(x);
                sizeItem.setIdentification(product_flag.getName());
                sizesBox.add(sizeItem);
                sizeRepository.save(sizeItem);
            }
        }


        product_flag.setColours(colorsBox);
        product_flag.setBrands(brandBox);
        product_flag.setSizes(sizesBox);

        productRepository.save(product_flag);
        return product_flag;
    }

    public ProductResponse updateProduct(ProductDtos request) {

        var product_flag = productRepository.findByName_(request.getName());

        if(product_flag != null){
            product_flag.setName(request.getName());
            product_flag.setDescription(request.getDescription());
            product_flag.setPrice(request.getPrice());
            product_flag.setQuantity(request.getQuantity());
            product_flag.setCategory(request.getCategory());

            product_flag.setNRating(0);

            product_flag.setSaleStatus(request.getSaleStatus());
            product_flag.setSalePrice(request.getSalePrice());
            product_flag.setNewStatus(true);

            product_flag.setCreateDate(new Date());
            product_flag.setCreatedByUserid(request.getCreatedByUserid());
        }


        List<Colors> colorsBox = new ArrayList<>();
        if (request.getColours() != null && request.getColours().size() > 0) {
            for (var x : request.getColours()) {
                Colors colorItem = new Colors();
                colorItem.setColor(x);
                colorItem.setIdentification(product_flag.getName());
                colorsBox.add(colorItem);
                colorsRepository.save(colorItem);
            }
        }

        List<Brands> brandBox = new ArrayList<>();
        if (request.getBrands() != null && request.getBrands().size() > 0) {
            for (var x : request.getColours()) {
                Brands brandItem = new Brands();
                brandItem.setBrand(x);
                brandItem.setIdentification(product_flag.getName());
                brandBox.add(brandItem);
                brandRepository.save(brandItem);
            }
        }

        List<Sizes> sizesBox = new ArrayList<>();
        if (request.getSizes() != null && request.getSizes().size() > 0) {
            for (var x : request.getColours()) {
                Sizes sizeItem = new Sizes();
                sizeItem.setSize(x);
                sizeItem.setIdentification(product_flag.getName());
                sizesBox.add(sizeItem);
                sizeRepository.save(sizeItem);
            }
        }

        List<ProductImg> productImagesBox = new ArrayList<>();
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
                productImagesBox.add(productImg);
                productImgRepository.save(productImg);
            }
        }

        product_flag.setColours(colorsBox);
        product_flag.setBrands(brandBox);
        product_flag.setSizes(sizesBox);
        product_flag.setImages(productImagesBox);

        productRepository.save(product_flag);
        var productResponse = ProductResponse.builder()
                .id(product_flag.getId())
                .images(productImagesBox)
                .colours(request.getColours())
                .sizes(request.getSizes())
                .brands(request.getBrands())
                .name(product_flag.getName())
                .description(product_flag.getDescription())
                .price(product_flag.getPrice())
                .quantity(product_flag.getQuantity())
                .category(product_flag.getCategory())
                .rating(product_flag.getRating())
                .nRating(product_flag.getNRating())
                .favourite(product_flag.getFavourite())
                .saleStatus(product_flag.getSaleStatus())
                .salePrice(product_flag.getSalePrice())
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
        for (var x : flag) {
            List<ProductImg> imgs = productImgRepository.findProductImgByProductId(x.getName());
            List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(x.getName());
            List<Colors> colorsList = colorsRepository.findColorsByIAndIdentification(x.getName());
            List<Brands> brandsList = brandRepository.findBrandsByIAndIdentification(x.getName());
            List<Sizes> sizeList = sizeRepository.findSizesByIAndIdentification(x.getName());

            List<String> colorsListRe = new ArrayList<>();
            List<String> brandsListRe = new ArrayList<>();
            List<String> sizeListRe = new ArrayList<>();
            for (var x_0: colorsList) { colorsListRe.add(x_0.getColor());}
            for (var x_0: brandsList) { brandsListRe.add(x_0.getBrand());}
            for (var x_0: sizeList) { sizeListRe.add(x_0.getSize());}

            var y = ProductResponse.builder()
                    .id(x.getId())
                    .images(imgs)
                    .colours(colorsListRe)
                    .sizes(sizeListRe)
                    .brands(brandsListRe)
                    .name(x.getName())
                    .description(x.getDescription())
                    .price(x.getPrice())
                    .quantity(x.getQuantity())
                    .category(x.getCategory())
                    .rating(x.getRating())
                    .nRating(x.getNRating())
                    .favourite(x.getFavourite())
                    .saleStatus(x.getSaleStatus())
                    .salePrice(x.getSalePrice())
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
        List<Colors> colorsList = colorsRepository.findColorsByIAndIdentification(boxItem.get().getName());
        List<Brands> brandsList = brandRepository.findBrandsByIAndIdentification(boxItem.get().getName());
        List<Sizes> sizeList = sizeRepository.findSizesByIAndIdentification(boxItem.get().getName());

        List<String> colorsListRe = new ArrayList<>();
        List<String> brandsListRe = new ArrayList<>();
        List<String> sizeListRe = new ArrayList<>();
        for (var x_0: colorsList) { colorsListRe.add(x_0.getColor());}
        for (var x_0: brandsList) { brandsListRe.add(x_0.getBrand());}
        for (var x_0: sizeList) { sizeListRe.add(x_0.getSize());}

        var productResponse = ProductResponse.builder()
                .id(boxItem.get().getId())
                .images(imgs)
                .colours(colorsListRe)
                .sizes(sizeListRe)
                .brands(brandsListRe)
                .name(boxItem.get().getName())
                .description(boxItem.get().getDescription())
                .price(boxItem.get().getPrice())
                .quantity(boxItem.get().getQuantity())
                .category(boxItem.get().getCategory())
                .rating(boxItem.get().getRating())
                .nRating(boxItem.get().getNRating())
                .favourite(boxItem.get().getFavourite())
                .saleStatus(boxItem.get().getSaleStatus())
                .salePrice(boxItem.get().getSalePrice())
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

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .rating(commentDto.getRating())
                .productQuality(commentDto.getProductQuality())
                .identification_pro(product.getName())
                .identification_user(commentDto.getUsername())
                .build();
        commentRepository.save(comment);
        return comment;
    }

    public ResponseMessageDtos updateComment (CommentDto commentDto,Integer comment_id) throws ChangeSetPersister.NotFoundException {
        Comment comment = commentRepository.findCommentById(comment_id);
        if(comment == null){
            return ResponseMessageDtos.builder().status(400).message("Error! Comment Do not exits").build();
        }
        if(!Objects.equals(comment.getIdentification_user(), commentDto.getUsername())){
            return ResponseMessageDtos.builder().status(400).message("Error! You are not the one commenting on this comment, Editing failed !!!").build();
        }
        comment.setContent(commentDto.getContent());
        comment.setRating(commentDto.getRating());
        comment.setProductQuality(commentDto.getProductQuality());
        commentRepository.save(comment);
        return ResponseMessageDtos.builder().status(200).message("Update Comment successfully !").build();
    }

    public CommentDto deleteCommentDto (Integer comment_id) throws ChangeSetPersister.NotFoundException {

        Comment comment = commentRepository.findById(comment_id).orElseThrow(()-> new ChangeSetPersister.NotFoundException());
        commentRepository.delete(comment);
        return null;
    }
}
