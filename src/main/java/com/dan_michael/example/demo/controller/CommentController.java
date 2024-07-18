package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CommentDto;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class CommentController {

    private final ProductService service;
    @PostMapping("/{product_id}/comments")
    public ResponseEntity<Comment> createComment(
            @RequestBody CommentDto comment,
            @PathVariable Integer product_id) throws ChangeSetPersister.NotFoundException {
        Comment createComment = service.createComment(comment,product_id);
        return new ResponseEntity<Comment>(createComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseMessageDtos> deleteComment(
            @PathVariable Integer comment_id) throws ChangeSetPersister.NotFoundException {
        service.deleteCommentDto(comment_id);
        return new ResponseEntity<ResponseMessageDtos>(new ResponseMessageDtos(200,"Delete comment successfully!"), HttpStatus.CREATED);
    }
}
