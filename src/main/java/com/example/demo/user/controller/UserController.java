package com.example.demo.user.controller;

import com.example.demo.user.controller.model.GetUserResponse;
import com.example.demo.user.controller.model.GetUsersResponse;
import com.example.demo.user.controller.model.PostUserRequest;
import com.example.demo.user.controller.model.PutUserRequest;
import com.example.demo.user.repository.model.User;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("user")
    public ResponseEntity<GetUserResponse> getLoggedUser() {
        Optional<User> user = service.findLoggedUser();
        return user.map(value -> ResponseEntity.ok(new GetUserResponse(value.getLogin())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("users")
    public GetUsersResponse getUsers() {
        return new GetUsersResponse(service.findAllLogins());
    }

    @PostMapping("users")
    public ResponseEntity<Void> postUser(@RequestBody PostUserRequest request) {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        User user = new User(request.getLogin(), bc.encode(request.getPassword()), List.of("User"));
        service.createUser(user);
        return ResponseEntity.created(URI.create("http://localhost:8080/api/users/" + user.getLogin())).build();
    }


    @PutMapping("user")
    public ResponseEntity<Void> putUser(@RequestBody PutUserRequest request) {
        Optional<User> user = service.findLoggedUser();
        if (user.isPresent()) {
            user.get().setPassword(request.getPassword());
            service.updateUser(user.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}