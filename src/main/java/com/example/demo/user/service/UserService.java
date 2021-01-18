package com.example.demo.user.service;

import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> findLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return this.repository.findById(username);
        } else {
            return Optional.empty();
        }
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public List<String> findAllLogins() {
        return repository.findLogin();
    }

    public Optional<User> findUser(String login) {
        return repository.findById(login);
    }

    public void createUser(User user) {
        repository.save(user);
    }

    public void updateUser(User user) {
        repository.save(user);
    }

    public void deleteUser(User user) {
        repository.delete(user);
    }

}
