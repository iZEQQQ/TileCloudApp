package com.example.demo.rating.controller.model;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PutRatingRequest {

    private int rating;

}
