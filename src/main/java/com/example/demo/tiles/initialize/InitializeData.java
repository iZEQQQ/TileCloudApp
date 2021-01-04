package com.example.demo.tiles.initialize;

import com.example.demo.tiles.service.TileService;
import com.example.demo.user.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

//TODO wypelnic danymi 2-3 plytki i uzytkownikow
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
     * Service for professions operations.
     */
    private final ProfessionService professionService;

    @Autowired
    public InitializedData(CharacterService characterService, UserService userService, ProfessionService professionService) {
        this.characterService = characterService;
        this.userService = userService;
        this.professionService = professionService;
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should
     * be created only once.
     */
    @PostConstruct
    private synchronized void init() {
        if (userService.find("admin").isEmpty()) {
            User admin = User.builder()
                    .login("admin")
                    .name("Adam")
                    .surname("Cormel")
                    .birthDate(LocalDate.of(1990, 10, 21))
                    .email("admin@simplerpg.example.com")
                    .password(Sha256Utility.hash("adminadmin"))
                    .build();

            User kevin = User.builder()
                    .login("kevin")
                    .name("Kevin")
                    .surname("Pear")
                    .birthDate(LocalDate.of(2001, 1, 16))
                    .email("kevin@example.com")
                    .password(Sha256Utility.hash("useruser"))
                    .build();

            User alice = User.builder()
                    .login("alice")
                    .name("Alice")
                    .surname("Grape")
                    .birthDate(LocalDate.of(2002, 3, 19))
                    .email("alice@example.com")
                    .password(Sha256Utility.hash("useruser"))
                    .build();

            userService.create(admin);
            userService.create(kevin);
            userService.create(alice);

            Profession bard = Profession.builder().name("Bard").build();
            Profession cleric = Profession.builder().name("Cleric").build();
            Profession warrior = Profession.builder().name("Warrior").build();
            Profession rogue = Profession.builder().name("Rogue").build();

            professionService.create(bard);
            professionService.create(cleric);
            professionService.create(warrior);
            professionService.create(rogue);

            Character calvian = Character.builder()
                    .name("Calvian")
                    .age(18)
                    .background("A yong bard with some infernal roots.")
                    .experience(0)
                    .level(1)
                    .profession(bard)
                    .charisma(16)
                    .constitution(12)
                    .strength(8)
                    .portrait(getResourceAsByteArray("avatar/calvian.png"))//package relative path
                    .user(kevin)
                    .build();

            Character uhlbrecht = Character.builder()
                    .name("Uhlbrecht")
                    .age(37)
                    .background("Quite experienced half-orc warrior.")
                    .experience(0)
                    .level(1)
                    .profession(warrior)
                    .charisma(8)
                    .constitution(10)
                    .strength(18)
                    .portrait(getResourceAsByteArray("avatar/uhlbrecht.png"))//package relative path
                    .user(kevin)
                    .build();

            Character eloise = Character.builder()
                    .name("Eloise")
                    .age(32)
                    .background("Human cleric.")
                    .experience(0)
                    .level(1)
                    .profession(cleric)
                    .charisma(8)
                    .constitution(12)
                    .strength(14)
                    .portrait(getResourceAsByteArray("avatar/eloise.png"))//package relative path
                    .user(alice)
                    .build();

            Character zereni = Character.builder()
                    .name("Zereni")
                    .age(20)
                    .background("Half elf rogue.")
                    .experience(0)
                    .level(1)
                    .profession(rogue)
                    .charisma(14)
                    .constitution(12)
                    .strength(10)
                    .portrait(getResourceAsByteArray("avatar/zereni.png"))//package relative path
                    .user(alice)
                    .build();

            characterService.create(calvian);
            characterService.create(uhlbrecht);
            characterService.create(eloise);
            characterService.create(zereni);
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