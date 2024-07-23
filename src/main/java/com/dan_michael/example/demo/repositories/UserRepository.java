package com.dan_michael.example.demo.repositories;

 import com.dan_michael.example.demo.model.entities.User;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;

 import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  @Query("SELECT pi FROM User pi WHERE pi.id = :id")
  User findById_create(@Param("id")Integer id);
}
