package com.dan_michael.example.demo.repositories;

import com.dan_michael.example.demo.model.entities.Comment;
import com.dan_michael.example.demo.model.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}