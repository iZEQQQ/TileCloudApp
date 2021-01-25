package com.example.demo.tiles.service;

import com.example.demo.tiles.repository.TileRepository;
import com.example.demo.tiles.repository.model.Tile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class TileService {

    private TileRepository repository;

    @Autowired
    public TileService(TileRepository repository) {
        this.repository = repository;
    }

    public List<Tile> findAllTiles(Pageable page) {
        return repository.findAll(page).getContent();
    }

    public List<Long> findAllIds() {
        return repository.findId();
    }

    public Long findTilesSize(){
        return repository.count();
    }


    public Optional<Tile> findTile(Long id) {
        return repository.findById(id);
    }

    public Optional<Tile> findTileByBody(Tile tile) {
        return repository.findTile(tile);
    }

    public void createTile(Tile tile) {
        repository.save(tile);
    }

    public void updateTile(Tile tile) {
        repository.save(tile);
    }

    public void deleteTile(Tile tile) {
        repository.delete(tile);
    }

}
