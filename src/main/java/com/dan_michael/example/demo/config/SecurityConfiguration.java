package com.dan_michael.example.demo.config;

import com.dan_michael.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

import static com.dan_michael.example.demo.model.Permission.*;
import static com.dan_michael.example.demo.model.entities.Role.ADMIN;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/user/**",
            "/api/v1/transactions/**",
            "/api/v1/carts/**",
            "/api/v1/orders/**",
            "/api/v1/email-sender/**",
            "/api/v1/payments/**",
            "/api/v1/global/**",
            "/api/v1/product/**",
            "/configuration/ui",
            "/configuration/security",
            "/api/v1/chat-box/**",
            "/index.html",
            "/app/**",
            "/user/**",
            "/users",
            "/ws/**",
            "/messages/**",
            "user.png",
            "img_4.png",
            "/api/v1/images/**",
            "/media/images/**",
            "api/v1/rest/**",
            "chat-bot"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {
                    cors.configurationSource(request -> {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Arrays.asList(Constants.Front_Host_5555, Constants.Front_Host_8888));
                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH"));
                        configuration.setAllowCredentials(true);
                        configuration.addAllowedHeader("*");
                        return configuration;
                    });
                })
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(Constants.Admin_Config_Path).hasAnyRole(ADMIN.name())
                                .requestMatchers(GET, Constants.Admin_Config_Path).hasAnyAuthority(ADMIN_READ.name())
                                .requestMatchers(POST, Constants.Admin_Config_Path).hasAnyAuthority(ADMIN_CREATE.name())
                                .requestMatchers(PUT, Constants.Admin_Config_Path).hasAnyAuthority(ADMIN_UPDATE.name())
                                .requestMatchers(PATCH, Constants.Admin_Config_Path).hasAnyAuthority(ADMIN_UPDATE.name())
                                .requestMatchers(DELETE, Constants.Admin_Config_Path).hasAnyAuthority(ADMIN_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl(Constants.Logout_Config_Path)
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
        ;
        return http.build();
    }
}
