package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.CommentDto;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.Product;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.repositories.CategoryRepository;
import com.dan_michael.example.demo.repositories.CommentRepository;
import com.dan_michael.example.demo.repositories.ProductImgRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.swagger2.mappers.ModelMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CommentRepository commentRepository;
//    private final ProductImgRepository productImgRepository;

//-------------------Product-----------------------------------
    public Product save(ProductDtos request) {

        var ob = repository.findByName(request.getName());

        if(ob.isPresent()){
            return null;
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
//                productImg.setProduct(product_flag); // Set the product reference
                productImages.add(productImg);
            }
        }

        var product_flag = Product.builder()
                .images(productImages)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .category(request.getCategory())
                .colour(request.getColour())
                .size(request.getSize())
                .nRating(0)
                .favourite(false)
                .saleStatus(request.getSaleStatus())
                .salePrice(request.getSalePrice())
                .newStatus(true)
                .createDate(new Date())
                .createdByUserid(request.getCreatedByUserid())
                .build();
        repository.save(product_flag);
        return product_flag;
    }


//    @Transactional()
//    public List<Product> findAll() {
//        List<Product> products = repository.findAll();
//
//        // Initialize images lazily if needed
//        products.forEach(product -> product.getImages().size()); // This triggers lazy loading of images
//
//        return products;
//    }
    public List<Product> findAll() {
        return repository.findAll();
    }

//    public List<Product> findAll() {
//        return repository.findAll();
//    }
//    public List<Product> findAll() {
//        return repository.findAllProductsWithImagesAndComments();
//    }

    public Optional<Product> findbyID(Integer id){return repository.findById(id);}

//-------------------comments-----------------------------------
    public Comment createComment (CommentDto commentDto,Integer product_id) throws ChangeSetPersister.NotFoundException {

        Product product = repository.findById(product_id).orElseThrow(()-> new ChangeSetPersister.NotFoundException());

        Comment comment = Comment.builder()
                .product(product)
                .comment_user(commentDto.getComment_user())
                .build();
        commentRepository.save(comment);
        return comment;
    }

    public CommentDto deleteCommentDto (Integer comment_id) throws ChangeSetPersister.NotFoundException {

        Comment comment = commentRepository.findById(comment_id).orElseThrow(()-> new ChangeSetPersister.NotFoundException());
        commentRepository.delete(comment);
        return null;
    }
}
