package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CrawlerController {


    @GetMapping("/crawl")
    public ResponseEntity<Void> scrapEplytki() throws IOException {
        String url = "https://www.eplytki.pl/plytki-lazienkowe.html?limit=72";
        while (true) {
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select(".flag-p.item.last");
            items.forEach(element -> {
                Elements a = element.select(".product-image");
                String title = a.attr("title");
                Elements img = a.select(".lazyload");
                String imgUrl = img.attr("data-src");
                Elements span = element.select(".price");
                String price = span.text();

//                System.out.println(title);
//                System.out.println(price);
//                System.out.println(imgUrl);
            });
            Elements a = doc.select(".next.i-next");
            System.out.println(a.attr("href"));
            System.out.println(a.size());
            if (a.size() > 0) {
                url = a.attr("href");
            } else {
                break;
            }


        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/scrap")
    public ResponseEntity<Void> scrap() throws IOException{
        String url = "https://domus-sklep.pl/1107-wszystkie-plytki";
        while (true) {
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select(".pro_outer_box");
            items.forEach(element -> {
                Elements img = element.select(".replace-2x.img-responsive.menu_pro_img");
                String title = img.attr("alt");
                String imgUrl = img.attr("src");
                Elements span = element.select(".price.product-price");
                String price = span.text();

//                System.out.println(title);
//                System.out.println(price);
//                System.out.println(imgUrl);
            });
            Elements li = doc.select(".pagination_next");
            Elements a = li.next();
            System.out.println(a.attr("href"));
            System.out.println(a.size());
            if (a.size() > 0) {
                url = a.attr("href");
            } else {
                break;
            }

        return ResponseEntity.ok().build();

    }




}
