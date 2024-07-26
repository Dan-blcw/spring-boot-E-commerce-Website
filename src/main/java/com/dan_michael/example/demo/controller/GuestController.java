package com.dan_michael.example.demo.controller;


import com.dan_michael.example.demo.model.dto.global.PaginationDto;
import com.dan_michael.example.demo.model.dto.ob.ProductListDtos;
import com.dan_michael.example.demo.service.ProductService;
import com.dan_michael.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//--------------------------Order----------------------------------


}
