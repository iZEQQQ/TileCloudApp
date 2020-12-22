package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CrawlerController {

    private RestTemplate restTemplate;

    @Autowired
    public CrawlerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/crawl")
    public ResponseEntity<Void> scrapTiles() {
        String htmlPage = restTemplate.getForEntity("https://www.eplytki.pl/plytki-lazienkowe.html", String.class).getBody();
        System.out.println(htmlPage);


        return ResponseEntity.ok().build();
    }


}
