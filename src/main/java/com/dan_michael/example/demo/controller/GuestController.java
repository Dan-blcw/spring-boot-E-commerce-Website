package com.dan_michael.example.demo.controller;


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
        var ob = service.findbyID(id);
        return ResponseEntity.ok(ob);
    }
    @GetMapping(value = "/list-ob")
    public ResponseEntity<?> get_detail(
            @RequestParam (required = false)Integer _limit

    ) {
        var list = service.findAll();
        return ResponseEntity.ok(list);
    }
//--------------------------Cart----------------------------------


//--------------------------Order----------------------------------


}
