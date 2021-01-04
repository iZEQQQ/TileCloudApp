package com.example.demo.user.repository.model;


import com.example.demo.tiles.repository.model.Tile;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(name="users")
public class User {

    @Id
    private String login;

    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Tile> reviewedTiles ;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_name"))
    @Column(name = "role_name")
    private List<String> roles;

}
