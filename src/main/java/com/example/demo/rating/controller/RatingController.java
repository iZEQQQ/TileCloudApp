package com.example.demo.rating.controller;

import com.example.demo.rating.controller.model.GetRatingResponse;
import com.example.demo.rating.repository.model.Rating;
import com.example.demo.rating.service.RatingService;
import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.tiles.service.TileService;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private RatingService ratingService;

    private UserService userService;

    private TileService tileService;

//    TODO in context with path [] threw exception [Request processing failed; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.util.ArrayList<?>] to type [@org.springframework.data.jpa.repository.Query java.util.List<com.example.demo.rating.repository.model.Rating>] for value '[1, 1, 1]'; nested exception is org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.Long] to type [@org.springframework.data.jpa.repository.Query com.example.demo.rating.repository.model.Rating]] with root cause

//    Napisac controller nie rozumiem czemu nie moge uzyc build

    @Autowired
    public RatingController(RatingService ratingService, UserService userService,
                            TileService tileService) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.tileService = tileService;

    }


    @GetMapping("{tileId}")
    public ResponseEntity<GetRatingResponse> getAverageRating(@PathVariable("tileId") Long tileId) {
        Optional<Tile> tile = tileService.findTile(tileId);
        if (tile.isPresent()) {
            List<Rating> ratings = ratingService.findAllRatingsByTile(tile.get());
            double average = ratings.stream().mapToInt(Rating::getRating).summaryStatistics().getAverage();
            return ResponseEntity.ok(new GetRatingResponse((int) average));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PutMapping
//    public ResponseEntity<PutRatingRequest> updateRating(@RequestBody PutRatingRequest request) {
//        Optional<User> user = userService.findLoggedUser();
//        if (user.isPresent()) {
//            List<Rating> rating = ratingService.findAllRatingsByUser(user.get());
//
//        }
//
//        return ResponseEntity.noContent().build();
////        } else {
////            return ResponseEntity.notFound().build();
////        }
//    }
}




