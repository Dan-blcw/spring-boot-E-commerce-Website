package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.response.SubCategoryResponse;
import com.dan_michael.example.demo.model.entities.SubEn.SubCategory;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CategoryDtos;
import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.repositories.CategoryRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
import com.dan_michael.example.demo.repositories.SupRe.SubCategoryRepository;
import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    private final ProductRepository productRepository;

    private final SubCategoryRepository subCategoryRepository;
    public SubCategoryResponse createCategory(CategoryDtos request) {

        var ob = repository.findCategoryByName(request.getCategoryName());

        if(ob.isPresent()){
            return null;
        }
        List<SubCategory> listBrands = new ArrayList<>();
        List<String> check = new ArrayList<>();
        for (var x:request.getBrands()) {
            if(!check.contains(x)){
                check.add(x);
                var save = SubCategory.builder()
                        .subCategoryName(x)
                        .identification(request.getCategoryName())
                        .build();
                listBrands.add(save);
                subCategoryRepository.save(save);
            }
        }
        Category category_flag = Category.builder()
                    .name(request.getCategoryName())
                    .brand(listBrands)
                    .image_url(request.image_url)
                    .sku(request.getSku())
                    .createdDate(new Date())
                    .status(request.getStatus())
                    .build();

        repository.save(category_flag);

        return SubCategoryResponse.builder()
                .id(category_flag.getId())
                .sku(category_flag.getSku())
                .name(category_flag.getName())
                .image_url(category_flag.getImage_url())
                .brands(check)
                .date(category_flag.getCreatedDate())
                .status(category_flag.getStatus())
                .build();
    }

    public SubCategoryResponse updateCategory(CategoryDtos request) {

        var category_flag = repository.findCategoryByName_(request.getCategoryName());
        List<SubCategory> listBrands = new ArrayList<>();
        List<String> check = new ArrayList<>();
        if(category_flag != null){
            subCategoryRepository.deleteByIdentification(category_flag.getName());
            for (var x:request.getBrands()) {
                if(!check.contains(x)){
                    check.add(x);
                    var save = SubCategory.builder()
                            .subCategoryName(x)
                            .identification(request.getCategoryName())
                            .build();
                    listBrands.add(save);
                    subCategoryRepository.save(save);
                }
            }
            category_flag.setBrand(listBrands);
            category_flag.setImage_url(request.image_url);
            category_flag.setSku(request.getSku());
            category_flag.setStatus(request.getStatus());
            repository.save(category_flag);
        }
        return SubCategoryResponse.builder()
                .id(category_flag.getId())
                .sku(category_flag.getSku())
                .name(category_flag.getName())
                .image_url(category_flag.getImage_url())
                .brands(check)
                .date(category_flag.getCreatedDate())
                .status(category_flag.getStatus())
                .build();
    }

    public List<SubCategoryResponse> listCategory() {
        List<SubCategoryResponse> list = new ArrayList<>();
        List<Category> categoryList = repository.findByActive();
//        System.out.println(categoryList);
        for (int i = 0; i < categoryList.size(); i++) {
            var x = categoryList.get(i);
            List<String> check = new ArrayList<>();
            var brands = subCategoryRepository.findBrandsByIAndIdentification(x.getName());
            for (var y: brands) {
                if(!check.contains(y.getSubCategoryName())){
                    check.add(y.getSubCategoryName());
                }
            }
            list.add(SubCategoryResponse.builder()
                    .id(x.getId())
                    .sku(x.getSku())
                    .name(x.getName())
                    .image_url(x.getImage_url())
                    .brands(check)
                    .date(x.getCreatedDate())
                    .status(x.getStatus())
                    .build());
        }
        return list;
    }

    public SubCategoryResponse detailCategory(Integer id) {
        var categoryDetail = repository.findById(id);
        var brands = subCategoryRepository.findBrandsByIAndIdentification(categoryDetail.get().getName());
        categoryDetail.get().setBrand(brands);
        List<String> check = new ArrayList<>();
        for (var y: brands) {
            if(!check.contains(y.getSubCategoryName())){
                check.add(y.getSubCategoryName());
            }
        }
        return SubCategoryResponse.builder()
                .id(categoryDetail.get().getId())
                .sku(categoryDetail.get().getSku())
                .name(categoryDetail.get().getName())
                .image_url(categoryDetail.get().getImage_url())
                .brands(check)
                .date(categoryDetail.get().getCreatedDate())
                .status(categoryDetail.get().getStatus())
                .build();
    }


    public ResponseMessageDtos removeCategory(Integer id) {
        var flag = repository.findById(id);
        if(flag.isPresent()){
            subCategoryRepository.deleteByIdentification(flag.get().getName());
            productRepository.deleteByCategory(flag.get().getName());
            repository.deleteById(id);
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Delete_Category_Success)
                    .build();
        }else {
            return ResponseMessageDtos.builder()
                    .status(400)
                    .message(Constants.Delete_Category_Fail)
                    .build();
        }
    }

    public ResponseMessageDtos removeBrand(Integer category_id, String subCategorysName) {
        var flag = repository.findCategoryById_(category_id);
        if(flag != null){
            subCategoryRepository.deleteBySubCategorysName(subCategorysName);
            productRepository.deleteBySubCategorysName(subCategorysName);
            flag.setBrand(subCategoryRepository.findBrandsByIAndIdentification(flag.getName()));
            repository.save(flag);
            return ResponseMessageDtos.builder()
                    .status(200)
                    .message(Constants.Delete_Brand_Success)
                    .build();
        }else {
            return ResponseMessageDtos.builder()
                    .status(400)
                    .message(Constants.Delete_Brand_Fail)
                    .build();
        }
    }

    public List<String> findBrandByCategoryID(Integer id) {
        var categoryDetail = repository.findById(id);
        var brands = subCategoryRepository.findBrandsByIAndIdentification(categoryDetail.get().getName());
        categoryDetail.get().setBrand(brands);
        List<String> check = new ArrayList<>();
        for (var y: brands) {
            if(!check.contains(y.getSubCategoryName())){
                check.add(y.getSubCategoryName());
            }
        }
        return check;
    }
}
