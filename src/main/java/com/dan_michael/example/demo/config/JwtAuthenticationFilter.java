package com.dan_michael.example.demo.config;

import com.dan_michael.example.demo.repositories.TokenRepository;
import com.dan_michael.example.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth") || request.getServletPath().contains("/api/v1/global")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // if authheader khong mo dau voi bearer thi lap tuc return,
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
        /* đại diện cho việc chuyển tiếp yêu cầu (request) và phản hồi (response)đến bộ lọc hoặc servlet tiếp theo trong chuỗi filter. Nếu không có
          lệnh này hoặc nếu lệnh này không được gọi, yêu cầu sẽ không tiếp tụcxử lý và cuối cùng không có phản hồi trả về cho người dùng.*/
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        /*  istokenvalid đóng vai trò như 1 bộ lọc, xem rằng liệu token mà chúng ta cócó còn được xác thực ?,
            trong trường hợp mã token hết hạn, thì đơn giản t lúc này sẽ trả về false */
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //  securitycontextholder đóng vai trò như là 1 bảo vệ giữ mã lệnh token, khi cần chúng ta sẽ lấy thông
                //  qua securitycontextholder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
