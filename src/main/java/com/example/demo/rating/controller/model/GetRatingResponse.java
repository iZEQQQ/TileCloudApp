package com.example.demo.rating.controller.model;

import com.example.demo.rating.repository.model.Rating;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetRatingResponse {

    private int rating;

    public static Function<Rating,GetRatingResponse> entityToDtoMapper(){
        return rating -> GetRatingResponse.builder()
                .rating(rating.getRating()).build();
    }

}
