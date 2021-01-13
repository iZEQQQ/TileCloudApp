package com.example.demo.controller;

import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.tiles.service.TileService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Component TODO for future scheduler conversion
@RestController
//Service TODO to future deployment
public class CrawlerController {

    private TileService service;

    @Autowired
    public CrawlerController(TileService service) {
        this.service = service;
    }

//    TODO  paginacje w springu i angularze
    //    @Scheduled(cron = "0 0 0 * * 0")
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
                Pattern pattern = Pattern.compile("\\d*,\\d{2}");
                Matcher matcher = pattern.matcher(price);
                matcher.find();
                price = matcher.group();
                price = price.replace(",", ".");
                byte[] imgBytes = null;
                try {
                    URL photoUrl = new URL(imgUrl);

                    try (InputStream stream = photoUrl.openStream()) {
                        imgBytes = stream.readAllBytes();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

                Tile brick = Tile.builder()
                        .name(title)
                        .photo(imgBytes)
//                        .type("Cegla")
                        .price(Double.parseDouble(price))
                        .build();

                service.createTile(brick);
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

    //    @Scheduled(fixedRate = 432000000)
    @GetMapping("/scrap")
    public ResponseEntity<Void> scrap() throws IOException {
        String url = "https://domus-sklep.pl/1107-wszystkie-plytki";
        while (true) {
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select(".pro_outer_box");
            items.forEach(element -> {
                Elements img = element.select(".replace-2x.img-responsive.menu_pro_img");
                Elements imgLazy = element.select(".lazy-loading.replace-2x.img-responsive.front-image");
                String imgUrlLazy = imgLazy.attr("data-src");
                String titleLazy = imgLazy.attr("title");
                String title = img.attr("alt");
                String imgUrl = img.attr("src");
                Elements span = element.select(".price.product-price");
                String price = span.text();

                System.out.println(title);
                System.out.println(titleLazy);
                System.out.println(price);
                System.out.println(imgUrl);
                System.out.println(imgUrlLazy);
            });
            Elements li = doc.select(".pagination_next > a");

            System.out.println(li.attr("href"));
            System.out.println(li.size());
            if (li.size() > 0) {
                url = "https://domus-sklep.pl" + li.attr("href");
            } else {
                break;
            }
        }
        return ResponseEntity.ok().build();
    }


}
