package com.example.demo.tiles.controller;

import com.example.demo.rating.service.RecommendationService;
import com.example.demo.tiles.controller.model.GetTileResponse;
import com.example.demo.tiles.controller.model.GetTilesResponse;
import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.tiles.service.TileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/tiles")
public class TileController {

    private final TileService service;

    private final RecommendationService recommService;

    @Autowired
    public TileController(TileService service, RecommendationService recommendationService) {
        this.service = service;
        this.recommService = recommendationService;
    }

    @GetMapping("{tileId}")
    public ResponseEntity<GetTileResponse> getTile(@PathVariable("tileId") Long tileId) {
        return service.findTile(tileId)
                .map(value -> ResponseEntity.ok(GetTileResponse.entityToDtoMapper().apply(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("recommended")
    public ResponseEntity<GetTilesResponse> getRecommended() {
        return ResponseEntity.ok(GetTilesResponse.entityToDtoMapper()
                .apply(recommService.findRecommendedTilesForCalledPrincipal()));
    }

    @GetMapping("")
    public ResponseEntity<GetTilesResponse> getTiles() {
        return ResponseEntity.ok(GetTilesResponse.entityToDtoMapper().apply(service.findAllTiles()));
    }

    @GetMapping(value = "{id}/photo", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getTilePhoto(@PathVariable("id") long id) {
        Optional<Tile> tile = service.findTile(id);
        return tile.map(value -> ResponseEntity.ok(value.getPhoto()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

