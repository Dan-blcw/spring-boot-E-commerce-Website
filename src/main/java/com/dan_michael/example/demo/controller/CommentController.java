package com.dan_michael.example.demo.controller;

import com.dan_michael.example.demo.model.dto.global.ResponseMessageDtos;
import com.dan_michael.example.demo.model.dto.ob.CommentDto;
import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class CommentController {

    private final ProductService service;


    @GetMapping("/{identification}/detail-comments-by-product")
    public ResponseEntity<?> DetailCommentByProduct(
            @PathVariable String identification
    ) throws ChangeSetPersister.NotFoundException {
        List<Comment> listComment = service.listCommentByIdentification_pro(identification);
        return new ResponseEntity<List<Comment>>(listComment, HttpStatus.OK);
    }

    @GetMapping("/{identification}/detail-comments-by-name")
    public ResponseEntity<?> DetailCommentByUser(
            @PathVariable String identification
    ) throws ChangeSetPersister.NotFoundException {
        List<Comment> listComment = service.listCommentByIdentification_user(identification);
        return new ResponseEntity<List<Comment>>(listComment, HttpStatus.OK);
    }

    @GetMapping("/List-comments")
    public ResponseEntity<?> listComment() throws ChangeSetPersister.NotFoundException {
        List<Comment> listComment = service.listComment();
        return new ResponseEntity<List<Comment>>(listComment, HttpStatus.OK);
    }
    @PostMapping("/{commment_id}/update-comments")
    public ResponseMessageDtos UpdateComment(
            @RequestBody CommentDto comment,
            @PathVariable Integer commment_id
    ) throws ChangeSetPersister.NotFoundException {
        ResponseMessageDtos responseMessageDtos = service.updateComment(comment,commment_id);
        return responseMessageDtos;
    }
    @PostMapping("/{product_id}/add-comments")
    public ResponseEntity<Comment> createComment(
            @RequestBody CommentDto comment,
            @PathVariable Integer product_id
    ) throws ChangeSetPersister.NotFoundException {
        Comment createComment = service.createComment(comment,product_id);
        return new ResponseEntity<Comment>(createComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseMessageDtos> deleteComment(
            @PathVariable Integer comment_id) throws ChangeSetPersister.NotFoundException {
        service.deleteCommentDto(comment_id);
        return new ResponseEntity<ResponseMessageDtos>(new ResponseMessageDtos(200,"Delete comment successfully!"), HttpStatus.GONE);
    }
}
