package com.possible.jwtsecurity.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.possible.jwtsecurity.model.AppUser;
import com.possible.jwtsecurity.model.Role;
import com.possible.jwtsecurity.security.JwtConfig;
import com.possible.jwtsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtConfig jwtConfig;

    @GetMapping("/users")
    public ResponseEntity<Page<AppUser>> findAllUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<AppUser> fetchUser(@PathVariable String username) throws Throwable {
        return new ResponseEntity<>(userService.getUser(username), HttpStatus.OK);
    }

    @PostMapping("/users/save")
    public ResponseEntity<AppUser> createUser(AppUser user){
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> assignRole(@RequestParam String username, @RequestParam String role){
        userService.addRoleToUser(username, role);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Throwable {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        long expiryTime = Long.parseLong(jwtConfig.getExpireTime());
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());

                Algorithm signAlgo =  Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes(StandardCharsets.UTF_8));
                JWTVerifier verifier = JWT.require(signAlgo).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                String username = decodedJWT.getSubject();
                AppUser user = userService.getUser(username);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + expiryTime))
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(signAlgo);
                Map<String, String> token = new HashMap<>();
                token.put("access_token", accessToken);
                token.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper(). writeValue(response.getOutputStream(), token);
            }catch (Exception ex){
                response.setHeader("error", ex.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> errors = new HashMap<>();
                errors.put("error_message", ex.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
        }
        else throw  new RuntimeException("Refresh Token is missing");
    }
}
