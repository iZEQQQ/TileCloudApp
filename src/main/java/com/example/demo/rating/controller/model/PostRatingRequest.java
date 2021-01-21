package com.example.demo.rating.controller.model;

import com.example.demo.rating.repository.model.Rating;
import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.user.repository.model.User;
import lombok.*;

import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostRatingRequest {

    private int rating;

    public static Function<PostRatingRequest, Rating> dtoToEntityMapper(
            Supplier<Tile> tileSupplier,
            Supplier<User> userSupplier) {
        return request -> Rating.builder()
                .rating(request.getRating())
                .tile(tileSupplier.get())
                .user(userSupplier.get())
                .build();
    }


}
