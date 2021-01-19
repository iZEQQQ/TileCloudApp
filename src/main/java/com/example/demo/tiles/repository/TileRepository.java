package com.example.demo.tiles.repository;

import com.example.demo.tiles.repository.model.Tile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TileRepository extends JpaRepository<Tile,Long> {

    @Query("select t.id from Tile t")
    List<Long> findId();

    @Query("select t from Tile t where t.type in :types")
    List<Tile> findAllByType(Collection<String> types);
}
