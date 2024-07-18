package com.dan_michael.example.demo.repositories;

 import com.dan_michael.example.demo.model.entities.User;
 import org.springframework.data.jpa.repository.JpaRepository;

 import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
}
