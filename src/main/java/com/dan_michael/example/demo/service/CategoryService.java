package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ob.sub.SubCategoryResponse;
import com.dan_michael.example.demo.model.entities.SubEn.Brand;
import com.dan_michael.example.demo.model.response.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CategoryDtos;
import com.dan_michael.example.demo.model.entities.Category;
import com.dan_michael.example.demo.repositories.CategoryRepository;
import com.dan_michael.example.demo.repositories.ProductRepository;
import com.dan_michael.example.demo.repositories.SupRe.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    private final ProductRepository productRepository;

    private final BrandRepository brandRepository;
    public SubCategoryResponse createCategory(CategoryDtos request) {

        var ob = repository.findCategoryByName(request.getCategoryName());

        if(ob.isPresent()){
            return null;
        }
        List<Brand> listBrands = new ArrayList<>();
        List<String> check = new ArrayList<>();
        for (var x:request.getBrands()) {
            if(!check.contains(x)){
                check.add(x);
                var save = Brand.builder()
                        .brand(x)
                        .identification(request.getCategoryName())
                        .build();
                listBrands.add(save);
                brandRepository.save(save);
            }
        }
        Category category_flag = Category.builder()
                    .name(request.getCategoryName())
                    .brand(listBrands)
                    .sku(request.getSku())
                    .createdDate(new Date())
                    .status(request.getStatus())
                    .build();

        repository.save(category_flag);

        return SubCategoryResponse.builder()
                .id(category_flag.getId())
                .sku(category_flag.getSku())
                .name(category_flag.getName())
                .brands(check)
                .date(category_flag.getCreatedDate())
                .status(category_flag.getStatus())
                .build();
    }

    public SubCategoryResponse updateCategory(CategoryDtos request) {

        var category_flag = repository.findCategoryByName_(request.getCategoryName());
        List<Brand> listBrands = new ArrayList<>();
        List<String> check = new ArrayList<>();
        if(category_flag != null){
            for (var x:request.getBrands()) {
                if(!check.contains(x)){
                    check.add(x);
                    listBrands.add(Brand.builder()
                            .brand(x)
                            .identification(request.getCategoryName())
                            .build());
                }
            }
            category_flag.setBrand(listBrands);
            category_flag.setSku(request.getSku());
            category_flag.setStatus(request.getStatus());
        }
        repository.save(category_flag);
        return SubCategoryResponse.builder()
                .id(category_flag.getId())
                .sku(category_flag.getSku())
                .name(category_flag.getName())
                .brands(check)
                .date(category_flag.getCreatedDate())
                .status(category_flag.getStatus())
                .build();
    }

    public List<SubCategoryResponse> listCategory() {
        List<SubCategoryResponse> list = new ArrayList<>();
        var categoryList = repository.findAll();
        for (var x:categoryList) {
            if(x.getStatus() == 1){
                List<String> check = new ArrayList<>();
                var brands = brandRepository.findBrandsByIAndIdentification(x.getName());
                for (var y: brands) {
                    if(!check.contains(y.getBrand())){
                        check.add(y.getBrand());
                    }
                }
                list.add(SubCategoryResponse.builder()
                        .id(x.getId())
                        .sku(x.getSku())
                        .name(x.getName())
                        .brands(check)
                        .date(x.getCreatedDate())
                        .status(x.getStatus())
                        .build());
            }else{
                categoryList.remove(x);
            }
        }
        return list;
    }

    public SubCategoryResponse detailCategory(Integer id) {
        var categoryDetail = repository.findById(id);
        var brands = brandRepository.findBrandsByIAndIdentification(categoryDetail.get().getName());
        categoryDetail.get().setBrand(brands);
        List<String> check = new ArrayList<>();
        for (var y: brands) {
            if(!check.contains(y.getBrand())){
                check.add(y.getBrand());
            }
        }
        return SubCategoryResponse.builder()
                .id(categoryDetail.get().getId())
                .sku(categoryDetail.get().getSku())
                .name(categoryDetail.get().getName())
                .brands(check)
                .date(categoryDetail.get().getCreatedDate())
                .status(categoryDetail.get().getStatus())
                .build();
    }


    public ResponseMessageDtos removeCategory(Integer id) {
        var flag = repository.findById(id);
        if(flag.isPresent()){
            brandRepository.deleteByIdentification(flag.get().getName());
            productRepository.deleteByCategory(flag.get().getName());
            repository.deleteById(id);
            return ResponseMessageDtos.builder().status(200).message("Delete Category successfully !!").build();
        }else {
            return ResponseMessageDtos.builder().status(400).message("Delete Category fail !!").build();
        }
    }

    public ResponseMessageDtos removeBrand(Integer category_id, String brandsName) {
        var flag = repository.findCategoryById_(category_id);
        if(flag != null){
            brandRepository.deleteBybrandName(brandsName);
            productRepository.deleteByBrands(brandsName);
            flag.setBrand(brandRepository.findBrandsByIAndIdentification(flag.getName()));
            repository.save(flag);
            return ResponseMessageDtos.builder().status(200).message("Delete Brand successfully !!").build();
        }else {
            return ResponseMessageDtos.builder().status(400).message("Delete Brand fail !!").build();
        }
    }

    public List<String> findBrandByCategoryID(Integer id) {
        var categoryDetail = repository.findById(id);
        var brands = brandRepository.findBrandsByIAndIdentification(categoryDetail.get().getName());
        categoryDetail.get().setBrand(brands);
        List<String> check = new ArrayList<>();
        for (var y: brands) {
            if(!check.contains(y.getBrand())){
                check.add(y.getBrand());
            }
        }
        return check;
    }
}
