package com.example.demo.rating.service;

import com.example.demo.rating.repository.RatingRepository;
import com.example.demo.rating.repository.model.Rating;
import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.user.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private RatingRepository repository;

    @Autowired
    public RatingService(RatingRepository repository) {
        this.repository = repository;
    }

    public List<Rating> findAllRatingsByTile(Tile tile) {
        return repository.findRatingsByTile(tile);
    }

    public List<Rating> findAllRatingsByUser(User user) {
        return repository.findRatingsByUser(user);
    }

    public Rating findSingleRatingByUserAndTile(User user, Tile tile) {
        return repository.findRatingByUserAndTile(tile, user);
    }

    public void createRating(Rating rating){
        repository.save(rating);
    }


}
