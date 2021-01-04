package com.example.demo.tiles.repository.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
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

    private String photo;

    private int price;

    private double rating;

    //    dodac enuma z typem
    public Tile(String name, String photo, int price) {
        this.name = name;
        this.photo = photo;
        this.price = price;
    }


}