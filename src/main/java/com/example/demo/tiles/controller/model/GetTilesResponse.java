package com.example.demo.tiles.controller.model;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetTilesResponse {

    @Getter
    @Setter
    @Builder
    @EqualsAndHashCode
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Tile{

            private Long id;

            private String name;

            private double price;

            private String page;

    }

    @Singular
    private List<Tile> tiles;


    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<com.example.demo.tiles.repository.model.Tile>, GetTilesResponse> entityToDtoMapper() {
        return tiles -> {
            GetTilesResponseBuilder response = GetTilesResponse.builder();
            tiles.stream()
                    .map(tile -> Tile.builder()
                            .id(tile.getId())
                            .name(tile.getName())
                            .price(tile.getPrice())
                            .page(tile.getPage())
                            .build())
                    .forEach(response::tile);
            return response.build();
        };
    }

}
