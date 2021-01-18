package com.example.demo.rating.repository.model;

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
//@Entity
@Table(name="raitings")
public class Rating {

    private int rating;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_name"))
    @Column(name = "role_name")
    private List<String> roles;


}
