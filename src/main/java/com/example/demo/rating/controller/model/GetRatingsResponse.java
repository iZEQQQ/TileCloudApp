package com.example.demo.rating.controller.model;

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
public class GetRatingsResponse {

    @Getter
    @Setter
    @Builder
    @EqualsAndHashCode
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Rating {

        private int rating;


    }

    @Singular
    List<Rating> ratings;


    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<com.example.demo.rating.repository.model.Rating>, GetRatingsResponse> entityToDtoMapper() {
        return ratings -> {
            GetRatingsResponseBuilder response = GetRatingsResponse.builder();
            ratings.stream()
                    .map(rating -> Rating.builder()
                            .rating(rating.getRating())
                            .build())
                    .forEach(response::rating);
            return response.build();
        };
    }

}
