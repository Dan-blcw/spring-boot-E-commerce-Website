package com.dan_michael.example.demo.controller;


import com.dan_michael.example.demo.model.dto.global.PaginationDto;
import com.dan_michael.example.demo.model.dto.ob.ProductDtos;
import com.dan_michael.example.demo.model.dto.ob.ProductListDtos;
import com.dan_michael.example.demo.model.dto.ob.sub.SubQuantityResponse;
import com.dan_michael.example.demo.model.response.ProductResponse;
import com.dan_michael.example.demo.service.ProductService;
import com.dan_michael.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/global")
@RequiredArgsConstructor
public class GuestController {

    private final ProductService service;

    private final UserService Change_service;

//--------------------------Read Only Product----------------------------------
    @GetMapping(value = "/detail-ob")
    public ResponseEntity<?> detail(
            @RequestParam (required = false)Integer id

    ) {
        var ob = service.findbyIDHander(id);
        return ResponseEntity.ok(ob);
    }
    @GetMapping(value = "/list-ob")
    public ResponseEntity<?> get_detail(
            @RequestParam (required = false)Integer _limit,
        @RequestParam (required = false)Integer _total
    ) {
        var list = service.findAllHander();
        return ResponseEntity.ok(ProductListDtos.builder().data(list).paginationDto(new PaginationDto(_total,_limit)).build());
    }
//--------------------------favorite----------------------------------
    @GetMapping(value = "/favorite-products")
    public ResponseEntity<?> getFavoriteByUser_id(
            @RequestParam (required = false)Integer use_id

    ) {
        var ob = service.findbyFavouriteByUserID(use_id);
        return ResponseEntity.ok(ob);
    }
    @PostMapping(value = "/add-favorite")
    public ResponseEntity<?> addFavorite(
            @RequestParam (required = false)String Product_name,
            @RequestParam (required = false)Integer use_id
    ) {
        var response = service.addFavourite(Product_name,use_id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete-favorite")
    public ResponseEntity<?> deleteFavorite(
            @RequestParam (required = false)String Product_name,
            @RequestParam (required = false)Integer use_id
    ) {
        var response = service.deleteFavourite(Product_name,use_id);
        return ResponseEntity.ok(response);
    }
//--------------------------QuantityDetail----------------------------------

    @GetMapping(value = "/delete-favorite")
    public ResponseEntity<?> getDetail(
            @RequestParam (required = false)String Product_name,
            @RequestParam (required = false)Integer use_id
    ) {
        var response = service.deleteFavourite(Product_name,use_id);
        return ResponseEntity.ok(response);
    }

//--------------------------Search----------------------------------
    @GetMapping(value = "/list-search",produces = "application/json")
    public ResponseEntity<?> global_search(
            @RequestParam (required = false)List<String> brands,
            @RequestParam (required = false)Boolean isPromotion,
            @RequestParam (required = false)Boolean isReleased,
            @RequestParam (required = false)Integer ratingGte,
            @RequestParam (required = false)Integer price_gte,
            @RequestParam (required = false)Integer price_lte,
            @RequestParam Integer _limit,
            @RequestParam Integer _page,
            @RequestParam (required = false)String _sort
    ) {
        List<ProductResponse> list = service.search_all(brands,isPromotion,isReleased,ratingGte,price_gte,price_lte,_sort);
        return ResponseEntity.ok(ProductListDtos.builder().data(list).paginationDto(new PaginationDto(list.size(),_limit)).build());
    }
//--------------------------GetQuantityByColorAndSize----------------------------------
    @GetMapping(value = "/detail-ob/{product_id}/get-quantity")
    public ResponseEntity<?> getQuantity(
            @PathVariable Integer product_id,
            @RequestParam (required = false)String color,
            @RequestParam (required = false)String size
    ) {
        var response = service.getQuantityByColorAndSize(product_id,color,size);
        return ResponseEntity.ok(response);
    }
//--------------------------Brands----------------------------------
    @GetMapping(value = "/list-ob/get-all-brands")
    public ResponseEntity<?> getAllBrands() {
        var response = service.getbrands();
        return ResponseEntity.ok(response);
    }

}
