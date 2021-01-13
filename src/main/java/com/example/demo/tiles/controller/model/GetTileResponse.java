package com.example.demo.tiles.controller.model;

import com.example.demo.tiles.repository.model.Tile;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetTileResponse {

    private Long id;

    private String name;

    private String type;

    private double price;

    private double rating;

    public static Function<Tile, GetTileResponse> entityToDtoMapper() {
        return tile -> GetTileResponse.builder()
                .id(tile.getId())
                .name(tile.getName())
                .type(tile.getType())
                .price(tile.getPrice())
                .build();
    }


}
