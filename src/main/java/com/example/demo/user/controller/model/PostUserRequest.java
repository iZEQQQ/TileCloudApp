package com.example.demo.user.controller.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostUserRequest {

    private String login;

    private String password;

}