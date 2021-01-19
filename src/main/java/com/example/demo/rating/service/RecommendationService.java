package com.example.demo.rating.service;

import com.example.demo.rating.repository.RatingRepository;
import com.example.demo.rating.repository.model.Rating;
import com.example.demo.tiles.repository.TileRepository;
import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.model.User;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private RatingRepository ratingRepository;

    private UserRepository userRepository;

    private UserService userService;

    private TileRepository tileRepository;

    @Autowired
    public RecommendationService(RatingRepository ratingRepository, UserRepository userRepository,
                                 UserService userService, TileRepository tileRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.tileRepository = tileRepository;
    }


    public List<Tile> findRecommendedTilesForCalledPrincipal() {
        Optional<User> user = userService.findLoggedUser();
        if (user.isPresent()) {
            List<Rating> userRating = ratingRepository.findRatingsByUserAndRating(user.get(), 3.0);
            Set<String> collect = userRating.stream().map(rating -> rating.getTile().getType()).collect(Collectors.toSet());
            return tileRepository.findAllByType(collect);
        } else {
            return List.of();
        }
    }

}
