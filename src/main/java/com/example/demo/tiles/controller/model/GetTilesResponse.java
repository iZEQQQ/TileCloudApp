package com.example.demo.tiles.controller.model;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetTilesResponse {

    private List<Long> ids = new ArrayList<>();

}
