package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.global.AuthenticationDtos;
import com.dan_michael.example.demo.model.entities.User;
import com.dan_michael.example.demo.repositories.TokenRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler{

  private final TokenRepository tokenRepository;
  private final UserRepository repository;
  @Override
  public void logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Authentication authentication) {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    jwt = authHeader.substring(7);
    var storedToken = tokenRepository.findByToken(jwt)
        .orElse(null);
//    var user = repository.findByID_(storedToken.getUser().getId());
//    user.setLast_login(new Date());
    System.out.println(storedToken);
    if (storedToken != null) {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      tokenRepository.save(storedToken);
      SecurityContextHolder.clearContext();
    }
  }

}
