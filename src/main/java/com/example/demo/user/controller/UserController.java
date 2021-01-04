package com.example.demo.user.controller;

import com.example.demo.user.controller.model.GetUserResponse;
import com.example.demo.user.controller.model.GetUsersResponse;
import com.example.demo.user.controller.model.PostUserRequest;
import com.example.demo.user.controller.model.PutUserRequest;
import com.example.demo.user.repository.model.User;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("{login}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("login") String login) {
        Optional<User> user = service.findUser(login);
        return user.map(value -> ResponseEntity.ok(new GetUserResponse(value.getLogin())))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("")
    public GetUsersResponse getUsers() {
        return new GetUsersResponse(service.findAllLogins());
    }

    @PostMapping("")
    public ResponseEntity<Void> postUser(@RequestBody PostUserRequest request) {
        User user = new User(request.getLogin(), request.getPassword());
        service.createUser(user);
        return ResponseEntity.created(URI.create("http://localhost:8080/api/users/" + user.getLogin())).build();
    }



    @PutMapping("{login}")
    public ResponseEntity<Void> putUser(@PathVariable("login") String login, @RequestBody PutUserRequest request) {
        Optional<User> user = service.findUser(login);
        if (user.isPresent()) {
            user.get().setPassword(request.getPassword());
            service.updateUser(user.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{login}")
    public ResponseEntity<Void> deleteUser(@PathVariable("login") String login) {
        Optional<User> user = service.findUser(login);
        if (user.isPresent()) {
            service.deleteUser(user.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}