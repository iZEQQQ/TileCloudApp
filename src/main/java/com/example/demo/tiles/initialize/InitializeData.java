package com.example.demo.tiles.initialize;

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


    @Autowired
    public InitializeData(TileService tileService, UserService userService) {
        this.tileService = tileService;
        this.userService = userService;
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
                    .password(bc.encode("admin"))
                    .roles(List.of("Admin", "User"))
                    .build();

            User kevin = User.builder()
                    .login("kevin")
                    .password(bc.encode("hello"))
                    .roles(List.of("User"))
                    .build();

            User alice = User.builder()
                    .login("alice")
                    .password(bc.encode("oof"))
                    .roles(List.of("User"))
                    .build();

            userService.createUser(admin);
            userService.createUser(kevin);
            userService.createUser(alice);

            Tile brick = Tile.builder()
                    .name("Malinowa")
                    .photo(getResourceAsByteArray("jpg/cegielka3.jpg"))
                    .type("Cegla")
                    .price(24.5)
                    .rating(4.5)
                    .build();

            Tile brickTwo = Tile.builder()
                    .name("Brazowa")
                    .photo(getResourceAsByteArray("jpg/cegielka8.jpg"))
                    .type("Cegla")
                    .price(66.6)
                    .rating(2.7)
                    .build();

            Tile brickThree = Tile.builder()
                    .name("Zielona")
                    .photo(getResourceAsByteArray("jpg/cegielka6.jpg"))
                    .type("Cegla")
                    .price(35.42)
                    .rating(2.5)
                    .build();

            Tile brickFour = Tile.builder()
                    .name("Czekoladowa")
                    .photo(getResourceAsByteArray("jpg/cegielka4.jpg"))
                    .type("Cegla")
                    .price(55.5)
                    .rating(5.0)
                    .build();

            tileService.createTile(brick);
            tileService.createTile(brickTwo);
            tileService.createTile(brickThree);
            tileService.createTile(brickFour);


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