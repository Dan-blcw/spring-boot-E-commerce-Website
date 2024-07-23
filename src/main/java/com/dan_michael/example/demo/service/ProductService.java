package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.global.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CommentDto;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.repositories.CommentRepository;
import com.dan_michael.example.demo.repositories.ProductImgRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
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

    private final ProductRepository repository;

    private final CommentRepository commentRepository;

    private final ProductImgRepository productImgRepository;

//-------------------Product-----------------------------------
    public Product createProduct(ProductDtos request) {

        var ob = repository.findByName(request.getName());

        if(ob.isPresent()){
            return null;
        }
        var product_flag = new Product();
        product_flag.setName(request.getName());
        product_flag.setDescription(request.getDescription());
        product_flag.setPrice(request.getPrice());
        product_flag.setQuantity(request.getQuantity());
        product_flag.setCategory(request.getCategory());
        product_flag.setColour(request.getColour());
        product_flag.setSize(request.getSize());
        product_flag.setNRating(0);
        product_flag.setSaleStatus(request.getSaleStatus());
        product_flag.setSalePrice(request.getSalePrice());
        product_flag.setNewStatus(true);
        product_flag.setCreateDate(new Date());
        product_flag.setCreatedByUserid(request.getCreatedByUserid());

        List<ProductImg> productImages = new ArrayList<>();
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
                productImages.add(productImg);
                productImgRepository.save(productImg);
            }
        }
        product_flag.setImages(productImages);
        repository.save(product_flag);
        return product_flag;
    }

    public Product updateProduct(ProductDtos request) {

        var oproduct_flag = repository.findByName_(request.getName());

        if(oproduct_flag != null){
            oproduct_flag.setName(request.getName());
            oproduct_flag.setDescription(request.getDescription());
            oproduct_flag.setPrice(request.getPrice());
            oproduct_flag.setQuantity(request.getQuantity());
            oproduct_flag.setCategory(request.getCategory());
            oproduct_flag.setColour(request.getColour());
            oproduct_flag.setSize(request.getSize());
            oproduct_flag.setNRating(0);
            oproduct_flag.setSaleStatus(request.getSaleStatus());
            oproduct_flag.setSalePrice(request.getSalePrice());
            oproduct_flag.setNewStatus(true);
            oproduct_flag.setCreateDate(new Date());
            oproduct_flag.setCreatedByUserid(request.getCreatedByUserid());
        }
        List<ProductImg> productImages = new ArrayList<>();

        if (request.getImages() != null && request.getImages().size() > 0) {
            for (MultipartFile imageFile : request.getImages()) {
                ProductImg productImg = new ProductImg();
                try {
                    productImg.setImage(imageFile.getBytes()); // Save image bytes
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle exception
                }
                productImg.setIdentification(oproduct_flag.getName()); // Set the product reference
                productImages.add(productImg);
                productImgRepository.save(productImg);
            }
        }
        oproduct_flag.setImages(productImages);
        repository.save(oproduct_flag);
        return oproduct_flag;
    }
    public void removeImageFromProduct(Integer productId, Integer imageId) {
        Product product = repository.findById(productId).orElse(null);
        if (product != null) {
            product.getImages().removeIf(image -> image.getId().equals(imageId));
            repository.save(product);
        }
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        var flag = repository.findAll();
        for (var x : flag) {
            List<ProductImg> imgs = productImgRepository.findProductImgByProductId(x.getName());
            List<Comment> commentsList = commentRepository.findCommentByIAndIdentification_pro(x.getName());
            x.setImages(imgs);
            x.setComments(commentsList);
            products.add(x);
        }
        return products;
    }

    public ResponseMessageDtos removebyId(Integer id) {
        repository.deleteById(id);
        return ResponseMessageDtos.builder()
                .status(200)
                .message("Delete Product successfully !")
                .build();
    }
    public Optional<Product> findbyID(Integer id){return repository.findByIdWithImages(id);}

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

        Product product = repository.findById(product_id).orElseThrow(()-> new ChangeSetPersister.NotFoundException());

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .identification_pro(product.getName())
                .identification_user(commentDto.getUsername())
                .build();
        commentRepository.save(comment);
        return comment;
    }

    public ResponseMessageDtos updateComment (CommentDto commentDto,Integer comment_id) throws ChangeSetPersister.NotFoundException {
        Comment comment = commentRepository.findCommentById(comment_id);
        if(comment == null){
            return ResponseMessageDtos.builder().status(400).message("Error! From Update Comment").build();
        }
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
        return ResponseMessageDtos.builder().status(200).message("Update Comment successfully !").build();
    }

    public CommentDto deleteCommentDto (Integer comment_id) throws ChangeSetPersister.NotFoundException {

        Comment comment = commentRepository.findById(comment_id).orElseThrow(()-> new ChangeSetPersister.NotFoundException());
        commentRepository.delete(comment);
        return null;
    }
}
