package com.example.demo.tiles.controller.model;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PutTileRequest {

    private String name;

    private String photo;

    private int price;

    private double rating;

}
