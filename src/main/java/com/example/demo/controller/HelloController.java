package com.example.demo.controller;

import com.example.demo.entity.Tile;
import com.example.demo.repository.TileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    private TileRepository repository;

    @Autowired
    public HelloController(TileRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/hello")
    public ResponseEntity<Tile> sayHello() {
        if (repository.findAll().isEmpty()) {
            Tile tile = new Tile();
            tile.setId(1l);
            repository.save(tile);
        }
        Tile kafelek = repository.findById(1l).orElseThrow();

        return ResponseEntity.ok(kafelek);
    }


}
