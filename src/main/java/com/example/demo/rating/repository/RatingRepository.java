package com.example.demo.rating.repository;

import com.example.demo.rating.repository.model.Rating;
import com.example.demo.rating.repository.model.RatingKey;
import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.user.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingKey> {

    @Query("select r from Rating r where r.user = :user")
    List<Rating> findRatingsByUser(User user);

    @Query("select r from Rating r where r.tile = :tile")
    List<Rating> findRatingsByTile(Tile tile);

    @Query("select r from Rating r where r.tile = :tile and r.user = :user")
    Rating findRatingByUserAndTile(Tile tile, User user);

    @Query("select r from Rating r where r.user = :user and r.rating > :rating")
    List<Rating> findRatingsByUserAndRating(User user, Double rating);

}
