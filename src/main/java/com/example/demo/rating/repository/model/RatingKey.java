package com.example.demo.rating.repository.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RatingKey implements Serializable {

    private String user;

    private Long tile;


}
