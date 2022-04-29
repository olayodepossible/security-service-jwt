package com.possible.jwtsecurity.controller;

import com.possible.jwtsecurity.model.AppUser;
import com.possible.jwtsecurity.model.Role;
import com.possible.jwtsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Page<AppUser>> findAllUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<AppUser> fetchUser(@PathVariable String username) throws Throwable {
        return new ResponseEntity<>(userService.getUser(username), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<AppUser> createUser(AppUser user){
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> assignRole(@RequestParam String username, @RequestParam String role){
        userService.addRoleToUser(username, role);
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
