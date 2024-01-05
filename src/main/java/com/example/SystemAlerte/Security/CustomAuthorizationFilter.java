package com.example.SystemAlerte.Security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final GetPropertiesBean getPropertiesBean;

    public CustomAuthorizationFilter(GetPropertiesBean getPropertiesBean) {

        this.getPropertiesBean = getPropertiesBean;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getServletPath().equals("/login")
                || request.getServletPath().equals("/Controller/refreshtoken")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(getPropertiesBean.getSecret().getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Stream<String> stream1 = Arrays.stream(roles);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream1.forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));

                    });
                    UsernamePasswordAuthenticationToken token_user = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(token_user);
                    filterChain.doFilter(request, response);

                } catch (Exception e) {
                    log.error("Error logging in : {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(org.springframework.http.HttpStatus.FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", e.getMessage());

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }

            } else {
                filterChain.doFilter(request, response);
            }
        }

    }

}
