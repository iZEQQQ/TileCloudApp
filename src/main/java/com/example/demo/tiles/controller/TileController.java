package com.example.demo.tiles.controller;

import com.example.demo.tiles.controller.model.GetTileResponse;
import com.example.demo.tiles.controller.model.GetTilesResponse;
import com.example.demo.tiles.controller.model.PostTileRequest;
import com.example.demo.tiles.controller.model.PutTileRequest;
import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.tiles.service.TileService;
import org.dom4j.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/branches")
public class TileController {

    private TileService service;

    @Autowired
    public TileController(TileService service) {
        this.service = service;
    }

    //TODO dodac typ z vision
    @GetMapping("{tileId}")
    public ResponseEntity<GetTileResponse> getTile(@PathVariable("tileId") Long tileId) {
        Optional<Tile> tile = service.findTile(tileId);
        return tile.map(value -> ResponseEntity.ok(
                new GetTileResponse(value.getId(),
                        value.getName(),
                        value.getPhoto(),
                        value.getPrice(),
                        value.getRating())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public GetTilesResponse getTiles() {
        return new GetTilesResponse(service.findAllIds());
    }

    @PostMapping("")
    public ResponseEntity<Void> postTile(@RequestBody PostTileRequest request) {
        Tile tile = new Tile(request.getName(), request.getPhoto(), request.getPrice());
        service.createTile(tile);
        return ResponseEntity.created(URI.create("http://localhost:8080/api/tiles/" + tile.getId())).build();
    }

    @PutMapping("{tileId}")
    public ResponseEntity<Void> putTile(@PathVariable("tileId") Long tileId, @RequestBody PutTileRequest request) {
        Optional<Tile> tile = service.findTile(tileId);
        if (tile.isPresent()) {
            tile.get().setName(request.getName());
            tile.get().setPhoto(request.getPhoto());
            tile.get().setPrice(request.getPrice());
            tile.get().setRating(request.getRating());
            service.updateTile(tile.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("{tileId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable("tileId") Long tileId) {
        Optional<Tile> tile = service.findTile(tileId);
        if (tile.isPresent()) {
            service.deleteTile(tile.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

