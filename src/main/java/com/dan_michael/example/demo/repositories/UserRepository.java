package com.dan_michael.example.demo.repositories;

 import com.dan_michael.example.demo.model.entities.User;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;

 import java.util.List;
 import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  @Query("SELECT pi FROM User pi")
  List<User> findALl_();

  @Query("SELECT pi FROM User pi WHERE pi.name = :name")
  User findByName_(@Param("name")String name);
  @Query("SELECT pi FROM User pi WHERE pi.email = :email")
  User findByEmail_(@Param("email")String email);

    @Query("SELECT pi FROM User pi WHERE pi.email = :email OR pi.name = :name")
    User findByEmail_Name(@Param("email")String email,@Param("name")String name);
  @Query("SELECT pi FROM User pi WHERE pi.id = :id")
  User findById_create(@Param("id")Integer id);

//  @Query("SELECT pi FROM User pi WHERE pi.role = :role")
//  List<User> findByRole(@Param("role")String role);

  @Query("SELECT pi FROM User pi WHERE pi.email = :email")
  User findByUserAndEmail(
          @Param("email")String email);
}
