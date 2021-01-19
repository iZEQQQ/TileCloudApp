package com.example.demo.rating.repository.model;

import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.user.repository.model.User;
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
@Table(name="raitings")
@IdClass(RatingKey.class)
public class Rating {

    private int rating;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_name")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "tile")
    private Tile tile;

}
