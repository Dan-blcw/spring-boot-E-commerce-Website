package com.dan_michael.example.demo.service;

import com.dan_michael.example.demo.model.dto.ChangePasswordDtos;
import com.dan_michael.example.demo.model.dto.ChangeProfileDtos;
import com.dan_michael.example.demo.model.entities.User;
import com.dan_michael.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    public void changeProfile(ChangeProfileDtos request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setIs_active(request.getIs_active());
        user.setLast_update(new Date());
        repository.save(user);
    }

    public String changePassword(ChangePasswordDtos request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getOld_password(), user.getPassword())) {
            return "Wrong password";
        }
        if (!request.getNew_password().equals(request.getRenew_password())) {
            return "Password are not the same";
        }
        user.setPassword(passwordEncoder.encode(request.getNew_password()));
        repository.save(user);
        return "changePassword successfully !!!";
    }

    public String grantRolePermissions(ChangePasswordDtos request, Principal connectedUser) {

        return "grantRolePermissions successfully !!!";
    }
}
