package com.example.SystemAlerte.Security;

import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.SystemAlerte.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter implements AuthenticationManager {
        private final AuthenticationManager auth;
        private final GetPropertiesBean getPropertiesBean;
        private final PasswordConfig passwordConfig;
        private final UserService userService;

        public CustomAuthFilter(AuthenticationManager auth, PasswordConfig passwordConfig, UserService userService,
                        GetPropertiesBean getPropertiesBean) {
                this.auth = auth;
                this.passwordConfig = passwordConfig;
                this.userService = userService;
                this.getPropertiesBean = getPropertiesBean;
        }

        // i extracted it
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                UserDetails userDetails = userService.loadUserByUsername(authentication.getName());
                log.info("userdetails : {}", userDetails.getAuthorities());
                if (userDetails == null || !passwordConfig.passwordEncoder()
                                .matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
                        String pwd = passwordConfig.passwordEncoder()
                                        .encode(authentication.getCredentials().toString());

                        log.info("Invalid username/password {}/////{}", pwd, userDetails.getPassword());
                        throw new BadCredentialsException("Invalid username/password");

                }

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                authentication.getPrincipal().toString(),
                                authentication.getCredentials().toString(),
                                authentication.getAuthorities());
                log.info("token is : {} ", token);
                return token;
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                        throws AuthenticationException {
                String username = request.getParameter("username");
                String password = request.getParameter("password");

                log.info("username is : {}", username);
                log.info("password is : {} ", password);

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

                log.info("token is : {} ", token);
                return this.authenticate(token);

        }

        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                        FilterChain chain,
                        Authentication authResult) throws IOException, ServletException {
                String string = authResult.getPrincipal().toString();
                // i can't cast the user so i loaded it with loaed userbyusername method
                log.info("helllo its meeeee  : {}", string);
                User user = (User) userService.loadUserByUsername(string);
                log.info("userget authorities : {},{},{},secret is {}", user.getAuthorities(), user.getUsername(),
                                string, getPropertiesBean.getSecret());
                Algorithm algorithm = Algorithm.HMAC256(getPropertiesBean.getSecret().getBytes());
                String access_token = JWT.create().withSubject(user.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis() + getPropertiesBean.getAccess()))
                                .withIssuer(request.getRequestURL().toString())
                                .withClaim("roles",
                                                user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                                                .collect(Collectors.toList()))
                                .sign(algorithm);

                String refresh_token = JWT.create().withSubject(user.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis() + getPropertiesBean.getRefresh()))
                                .withIssuer(request.getRequestURL().toString())
                                .withClaim("roles",
                                                user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                                                .collect(Collectors.toList()))
                                .sign(algorithm);

                response.setHeader("access_token", access_token);
                response.setHeader("refresh_token", refresh_token);
                log.info("access_token is : {}", access_token);
                log.info("refresh_token is : {}", refresh_token);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

        }

}
