package com.dan_michael.example.demo.model.entities;

import com.dan_michael.example.demo.model.entities.img.UserImg;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;
  private String name;
  private String username;
  private String email;
  private String password;

  //Thêm
  private String companyName;
  private String address;
  private String phoneNumber;
  private Date date_joined;
  private Date last_login;
  private Date last_update;
  private Integer is_active;

  private Boolean useFirstDiscount;

  @OneToOne(mappedBy = "user",fetch = FetchType.EAGER)
//  @JsonBackReference
  private UserImg userImg;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @OneToMany(mappedBy = "comment_user")
  private Set<Comment> comments;
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + (password != null ? "****" : null) + '\'' +  // Masking the password
            ", companyName='" + companyName + '\'' +
            ", address='" + address + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", dateJoined=" + date_joined +
            ", lastLogin=" + last_login +
            ", lastUpdate=" + last_update +
            ", isActive=" + is_active +
            ", useFirstDiscount=" + useFirstDiscount +
            ", role=" + role +
            '}';
  }
}
