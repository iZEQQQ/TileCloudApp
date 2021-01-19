package com.example.demo.tiles.initialize;

import com.example.demo.rating.repository.model.Rating;
import com.example.demo.rating.service.RatingService;
import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.tiles.service.TileService;
import com.example.demo.user.repository.model.User;
import com.example.demo.user.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Component
public class InitializeData {

    /**
     * Service for tiles operations.
     */
    private final TileService tileService;

    /**
     * Service for users operations.
     */
    private final UserService userService;

    /**
     * Service for rating operations.
     */
    private final RatingService ratingService;

    @Autowired
    public InitializeData(TileService tileService, UserService userService, RatingService ratingService) {
        this.tileService = tileService;
        this.userService = userService;
        this.ratingService = ratingService;
    }


    /**
     * Initializes database with some example values. Should be called after creating this object. This object should
     * be created only once.
     */
    @PostConstruct
    private synchronized void init() {
        if (userService.findUser("admin").isEmpty()) {
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            User admin = User.builder()
                    .login("admin")
                    .password(bc.encode("Adm!n1234"))
                    .roles(List.of("Admin", "User"))
                    .build();

            User kevin = User.builder()
                    .login("kevin")
                    .password(bc.encode("U$er1234"))
                    .roles(List.of("User"))
                    .build();

            User alice = User.builder()
                    .login("alice")
                    .password(bc.encode("U$er1234"))
                    .roles(List.of("User"))
                    .build();

            userService.createUser(admin);
            userService.createUser(kevin);
            userService.createUser(alice);

            Tile brick = Tile.builder()
                    .name("Malinowa")
                    .photo(getResourceAsByteArray("/jpg/cegielka3.jpg"))
                    .type("Cegla")
                    .price(24.5)
                    .build();

            Tile brickTwo = Tile.builder()
                    .name("Brazowa")
                    .photo(getResourceAsByteArray("/jpg/cegielka8.jpg"))
                    .type("Cegla")
                    .price(66.6)
                    .build();

            Tile brickThree = Tile.builder()
                    .name("Zielona")
                    .photo(getResourceAsByteArray("/jpg/cegielka6.jpg"))
                    .type("Cegla")
                    .price(35.42)
                    .build();

            Tile brickFour = Tile.builder()
                    .name("Czekoladowa")
                    .photo(getResourceAsByteArray("/jpg/cegielka4.jpg"))
                    .type("Cegla")
                    .price(55.5)
                    .build();

            tileService.createTile(brick);
            tileService.createTile(brickTwo);
            tileService.createTile(brickThree);
            tileService.createTile(brickFour);

            Rating ratingOne = Rating.builder().rating(2).user(admin).tile(brick).build();
            Rating ratingT = Rating.builder().rating(3).user(alice).tile(brickTwo).build();
            Rating ratingY = Rating.builder().rating(4).user(kevin).tile(brickThree).build();
            Rating ratingU = Rating.builder().rating(4).user(kevin).tile(brickFour).build();
            Rating ratingO = Rating.builder().rating(4).user(admin).tile(brickTwo).build();
            Rating ratingF = Rating.builder().rating(5).user(alice).tile(brick).build();
            Rating ratingI = Rating.builder().rating(2).user(kevin).tile(brick).build();
            Rating ratingG = Rating.builder().rating(3).user(kevin).tile(brickTwo).build();
            Rating ratingP = Rating.builder().rating(1).user(admin).tile(brickThree).build();
            Rating ratingK = Rating.builder().rating(3).user(admin).tile(brickFour).build();
            Rating ratingPe = Rating.builder().rating(5).user(alice).tile(brickThree).build();
            Rating ratingRne = Rating.builder().rating(4).user(alice).tile(brickFour).build();

            ratingService.createRating(ratingG);
            ratingService.createRating(ratingF);
            ratingService.createRating(ratingI);
            ratingService.createRating(ratingK);
            ratingService.createRating(ratingO);
            ratingService.createRating(ratingOne);
            ratingService.createRating(ratingP);
            ratingService.createRating(ratingPe);
            ratingService.createRating(ratingRne);
            ratingService.createRating(ratingT);
            ratingService.createRating(ratingU);
            ratingService.createRating(ratingY);

        }
    }

    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            return is.readAllBytes();
        }
    }
}