package com.miuky.ewallet.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miuky.ewallet.dto.response.ApiResponse;
import com.miuky.ewallet.dto.response.ErrorResponse;
import com.miuky.ewallet.exception.AppException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component @RequiredArgsConstructor @Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJwtFromRequest(request);
            log.info("Token: {}", token);
            if (StringUtils.hasText(token)) {
                Claims claims = jwtService.validateToken(token);
                String phoneNumber = claims.getSubject();
                log.info("Validate token successfully: {}", claims.getSubject());
                UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Tới đây chưa");
            }
        } catch (AppException ex) {
            sendErrorResponse(request, response, ex);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, AppException ex) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        var body = ApiResponse.fail(ErrorResponse.build(ex.getErrorCode(), request));
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
