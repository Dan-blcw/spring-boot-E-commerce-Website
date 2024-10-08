package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
//    @Transactional
    @Query("SELECT pi FROM Comment pi WHERE pi.id = :Id AND pi.statusActive = 1")
    Comment findCommentById(@Param("Id")Integer Id);


    @Query("SELECT pi FROM Comment pi WHERE pi.statusActive = 0  AND pi.identification_order = :identification_order")
    List<Comment> findCommentStatus0(
                                     @Param("identification_order") Integer identification_order
    );
//
//    @Query("SELECT pi FROM Comment pi WHERE pi.content IS NULL OR pi.productQuality IS NULL")
//    List<Comment> findCommentUnfinished();
    @Query("SELECT pi FROM Comment pi WHERE pi.content IS NOT NULL AND pi.identification_user = :identification_user AND pi.statusActive = 1")
    List<Comment> findCommentCompleted(@Param("identification_user") String identification_user);


    @Query("SELECT pi FROM Comment pi WHERE pi.content IS NOT NULL")
    List<Comment> findAllCommentCompleted();
    @Query("SELECT pi FROM Comment pi WHERE pi.content IS NULL AND pi.identification_user = :identification_user AND pi.statusActive = 1")
    List<Comment> findCommentUnfinished(@Param("identification_user") String identification_user);
    @Query("SELECT pi FROM Comment pi WHERE pi.identification_pro = :identification_pro AND pi.content IS NOT NULL AND pi.statusActive = 1")
    List<Comment> findCommentByIAndIdentification_pro(@Param("identification_pro") String identification_pro);

    @Query("SELECT pi FROM Comment pi WHERE pi.identification_user = :identification_user AND pi.statusActive = 1")
    List<Comment> findCommentByIAndIdentification_user(@Param("identification_user") String identification_user);

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment od WHERE od.identification_pro = :identification_pro")
    void deleteByIdentification(@Param("identification_pro") String identification_pro);
}