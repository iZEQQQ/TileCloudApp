package com.example.demo.tiles.repository.model;

import lombok.*;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private byte[] photo;

    private String type;

    private double price;

    public Tile(String name, byte[] photo ,String type , double price) {
        this.name = name;
        this.photo = photo;
        this.type = type;
        this.price = price;
    }


}