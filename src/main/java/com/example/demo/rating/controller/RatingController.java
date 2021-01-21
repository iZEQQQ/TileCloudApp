package com.example.demo.rating.controller;

import com.example.demo.rating.controller.model.GetRatingsResponse;
import com.example.demo.rating.controller.model.PostRatingRequest;
import com.example.demo.rating.repository.model.Rating;
import com.example.demo.rating.service.RatingService;
import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.tiles.service.TileService;
import com.example.demo.user.repository.model.User;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/api/tiles/{tileId}/ratings")
public class RatingController {

    private RatingService ratingService;

    private UserService userService;

    private TileService tileService;

//    Napisac controller nie rozumiem czemu nie moge uzyc build

    @Autowired
    public RatingController(RatingService ratingService, UserService userService,
                            TileService tileService) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.tileService = tileService;

    }

    @GetMapping
    public ResponseEntity<GetRatingsResponse> getTileRatings(@PathVariable("tileId") Long tileId) {
        Optional<Tile> tile = tileService.findTile(tileId);
        return tile.map(value -> ResponseEntity.ok(GetRatingsResponse.entityToDtoMapper()
                .apply(ratingService.findAllRatingsByTile(value))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PostMapping
//    public ResponseEntity<Void> createRating(@PathVariable("tileId") Long tileId,
//                                             @RequestBody PostRatingRequest request,
//                                             UriComponentsBuilder builder) {
//        Optional<User> user = userService.findLoggedUser();
//        if (user.isPresent()) {
//            Rating rating = PostRatingRequest
//                    .dtoToEntityMapper(tileService.findTile(tileId).orElseThrow(), userService.findLoggedUser().orElseThrow())
//                    .apply(request);
//            rating = ratingService.createRating(rating);
//            return ResponseEntity.created("duno").build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

}




