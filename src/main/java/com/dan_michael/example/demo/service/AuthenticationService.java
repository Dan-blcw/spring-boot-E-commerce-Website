package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.AuthenticationDtos;
import com.dan_michael.example.demo.model.dto.global.RegisterDtos;
import com.dan_michael.example.demo.model.dto.global.SignInDtos;
import com.dan_michael.example.demo.model.entities.Role;
import com.dan_michael.example.demo.model.entities.Token;
import com.dan_michael.example.demo.model.entities.TokenType;
import com.dan_michael.example.demo.model.entities.User;
import com.dan_michael.example.demo.repositories.TokenRepository;
import com.dan_michael.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationDtos register(RegisterDtos request) {
        var user = User.builder()
                .name(request.getName())
                .username(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .companyName(null)
                .address(null)
                .phoneNumber(null)
                .date_joined(new Date())
                .last_login(null)
                .is_active(1)
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationDtos.builder()
                .jwt(jwtToken)
                .user(user)
                .build();
    }

    public AuthenticationDtos createAdmin(RegisterDtos request) {
        var Admin = User.builder()
                .name(request.getName())
                .username(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .companyName(null)
                .address(null)
                .phoneNumber(null)
                .date_joined(new Date())
                .last_login(null)
                .is_active(1)
                .build();
        var savedUser = repository.save(Admin);
        var jwtToken = jwtService.generateToken(Admin);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationDtos.builder()
                .jwt(jwtToken)
                .user(Admin)
                .build();
    }

    public AuthenticationDtos createManage(RegisterDtos request) {
        var manage = User.builder()
                .name(request.getName())
                .username(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MANAGE)
                .companyName(null)
                .address(null)
                .phoneNumber(null)
                .date_joined(new Date())
                .last_login(null)
                .is_active(1)
                .build();
        var savedUser = repository.save(manage);
        var jwtToken = jwtService.generateToken(manage);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationDtos.builder()
                .jwt(jwtToken)
                .user(manage)
                .build();
    }

    public AuthenticationDtos Login(SignInDtos request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var user1 = User.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .address(user.getAddress())
                .companyName(user.getCompanyName())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .date_joined(user.getDate_joined())
                .last_login(user.getLast_login())
                .is_active(user.getIs_active())
                .build();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationDtos.builder()
                .jwt(jwtToken)
                .user(user1)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
