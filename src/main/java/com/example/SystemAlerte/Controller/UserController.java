package com.example.SystemAlerte.Controller;

import java.io.IOException;
import java.net.URI;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.SystemAlerte.Model.Role;
import com.example.SystemAlerte.Model.User;
import com.example.SystemAlerte.Security.GetPropertiesBean;
import com.example.SystemAlerte.Security.PasswordConfig;
import com.example.SystemAlerte.Service.UserService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/Controller")
@RequiredArgsConstructor
@Slf4j

public class UserController {

  private final UserService userService;

  private final PasswordConfig passwordEncoder;
  private final GetPropertiesBean getPropertiesBean;

  @GetMapping("/Dashboard/users")
  public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok().body(userService.getUsers());

  }
  @GetMapping("/Front/get_user/{username}")
  public ResponseEntity <User> getUser(@PathVariable String username) {
    return ResponseEntity.ok().body(userService.getUser(username));

  }


  @PostMapping("/Front/user/Register")
  public ResponseEntity<User> SaveUser(@RequestBody User user) {
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/Controller/Front/user/Register").toUriString());
    String password = user.getPassword();
    log.info("password = {}", password);
    user.setPassword(passwordEncoder.passwordEncoder().encode(password));
    ResponseEntity<User> Response= ResponseEntity.created(uri).body(userService.SaveUser(user));
    userService.AddRoleToUser(user.getUsername(),"ROLE_USER");
    return Response;

  }

  @PostMapping("/Dashboard/role/save")
  public ResponseEntity<Role> SaveRole(@RequestBody Role role) {
    URI uri = URI.create(
        ServletUriComponentsBuilder.fromCurrentContextPath().path("/Controller/Dashboard/role/save").toUriString());
    return ResponseEntity.created(uri).body(userService.SaveRole(role));

  }

  @PostMapping("/Dashboard/role/addtouser")
  public ResponseEntity<?> AddRoleToUser(@RequestBody RoleToUserForm form) {
    userService.AddRoleToUser(form.getUsername(), form.getRolename());
    return ResponseEntity.ok().build();

  }

  @DeleteMapping("/Front/user/delete/{id}")
  public void DeleteUser(@PathVariable Long id) {
    userService.DeleteUser(id);
  }

  @PutMapping("/Front/user/update")
  public ResponseEntity<User> UpdateUser(@RequestBody User user) {
    return ResponseEntity.ok().body(userService.UpdateUser(user));
  }

  @PostMapping("/refreshtoken")
  public void RefreshToken(HttpServletRequest request, HttpServletResponse response)
      throws StreamWriteException, DatabindException, IOException {
    String authorizationHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      try {
        String refresh_token = authorizationHeader.substring("Bearer ".length());

        Algorithm algorithm = Algorithm.HMAC256(getPropertiesBean.getSecret().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        String username = decodedJWT.getSubject();
        User user = userService.getUser(username);
        String access_token = JWT.create().withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + getPropertiesBean.getAccess()))
            .withIssuer(request.getRequestURL().toString())
            .withClaim("roles",
                user.getRoles().stream().map(Role::getName)
                    .collect(Collectors.toList()))
            .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
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
      throw new RuntimeException("Refresh token is missing ");
    }
  }

  @Data
  static class RoleToUserForm {
    private String username;
    private String rolename;
  }
}