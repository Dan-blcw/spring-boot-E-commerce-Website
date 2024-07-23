package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.Order;
import com.dan_michael.example.demo.model.entities.ProductImg;
import com.dan_michael.example.demo.model.entities.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
//    @Transactional
//    @Query("SELECT pi FROM Comment pi WHERE pi.identification_pro = :name")
    Comment findCommentById(Integer Id);

    @Transactional
    @Query("SELECT pi FROM Comment pi WHERE pi.identification_pro = :identification_pro")
    List<Comment> findCommentByIAndIdentification_pro(@Param("identification_pro") String identification_pro);

    @Transactional
    @Query("SELECT pi FROM Comment pi WHERE pi.identification_user = :identification_user")
    List<Comment> findCommentByIAndIdentification_user(@Param("identification_user") String identification_user);
}