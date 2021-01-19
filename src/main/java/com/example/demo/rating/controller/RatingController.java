package com.example.demo.rating.controller;

import com.example.demo.rating.controller.model.GetRatingResponse;
import com.example.demo.rating.controller.model.PutRatingRequest;
import com.example.demo.rating.service.RatingService;
import com.example.demo.tiles.controller.model.GetTilesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingController {

    private RatingService ratingService;

    @GetMapping("{tileId}")
    public ResponseEntity<GetRatingResponse> getRating(@PathVariable("tileId") Long tileId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<GetTilesResponse> getAverageRatings() {
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<PutRatingRequest> updateRating(@RequestBody PutRatingRequest request) {
//           Optional<Rating> rating= ratingService;
//        if (rating.isPresent()) {

            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }
}




