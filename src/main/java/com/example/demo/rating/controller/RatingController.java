package com.example.demo.rating.controller;

import com.example.demo.rating.controller.model.GetRatingResponse;
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

import java.util.Optional;
@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/tiles/{tileId}/rating")
public class RatingController {

    private RatingService ratingService;

    private UserService userService;

    private TileService tileService;


    @Autowired
    public RatingController(RatingService ratingService, UserService userService,
                            TileService tileService) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.tileService = tileService;

    }

    @GetMapping
    public ResponseEntity<GetRatingResponse> getTileRatings(@PathVariable("tileId") Long tileId) {
        Optional<Tile> tile = tileService.findTile(tileId);
        return tile.map(value -> ResponseEntity.ok(GetRatingResponse.builder()
                .rating(ratingService.countRatingsByTile(value)).build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("add")
    public ResponseEntity<Void> createRating(@PathVariable("tileId") Long tileId,
                                             @RequestBody PostRatingRequest request) {
        Optional<User> user = userService.findLoggedUser();
        if (user.isPresent()) {
            Optional<Tile> tile = tileService.findTile(tileId);
            if (tile.isPresent()) {
                ratingService.createRating(tile.get(), user.get(), request.getRating());
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}




