package com.example.demo.tiles.repository.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tiles")
public class Tile {

    @Id
    private Long id;

    private String name;

    private byte[] photo;

    private String type;

    private double price;

    private double rating;



    public Tile(String name, byte[] photo ,String type , double price) {
        this.name = name;
        this.photo = photo;
        this.type = type;
        this.price = price;
    }


}