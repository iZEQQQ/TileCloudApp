package com.example.demo.user.authentication.service;

import com.example.demo.user.authentication.model.UserDetailsAdapter;
import com.example.demo.user.repository.model.User;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProviderService implements UserDetailsService {

    private final UserService service;

    @Autowired
    public UserProviderService(UserService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = service.findUser(login);
        if(user.isEmpty()){
            throw new UsernameNotFoundException(login);
        }
        return new UserDetailsAdapter(user.get());
    }




}
