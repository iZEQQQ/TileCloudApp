package com.example.demo.controller;

import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.tiles.service.TileService;
import com.google.cloud.automl.v1.*;
import com.google.protobuf.ByteString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@RestController
@Service
//TODO to future deployment
public class CrawlerController {

    private TileService service;

    @Autowired
    public CrawlerController(TileService service) {
        this.service = service;
    }

//    TODO  paginacje w springu i angularze oraz zrobic jeszcze crawlera na drugi sklep by uzupelnial wszystko
//     oraz dodawanie do bazy danych widok uzytkownika
//    TODO dodac po rejestracji mozliwosc oceniania
//    TODO dodac lokalizajce z ktorego sklepu zebrano
    //Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception
//
//org.springframework.security.web.firewall.RequestRejectedException: The request was rejected because the URL contained a potentially malicious String "//"
//    czasem to wyskakuje ????
    @PostConstruct
    public void init() throws IOException {
        scrapEplytki();
    }


    @Scheduled(cron = "0 0 * * 0")
    @GetMapping("/crawl")
    public ResponseEntity<Void> scrapEplytki() throws IOException {
        String url = "https://www.eplytki.pl/plytki-lazienkowe.html?limit=72";
//        while (true) {
//TODO przestawic na 4 strony finalnie
        for (int i = 0; i < 1; i++) {


            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select(".flag-p.item.last");
            items.stream().limit(4).forEach(element -> {
//                TODO odblokawac do wszystkich plytek z strony i usunanc ta wyzej
//            items.forEach(element -> {
                Elements a = element.select(".product-image");
                String title = a.attr("title");
                Elements img = a.select(".lazyload");
                String imgUrl = img.attr("data-src");
                Elements span = element.select(".price");
                String price = span.text();
                String type = null;
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
                if (imgBytes != null) {
                    try (PredictionServiceClient client = PredictionServiceClient.create()) {
                        ModelName name = ModelName.of("217603025665", "us-central1", "ICN616089350391726080");
                        ByteString content = ByteString.copyFrom(imgBytes);
                        Image image = Image.newBuilder().setImageBytes(content).build();
                        ExamplePayload payload = ExamplePayload.newBuilder().setImage(image).build();
                        PredictRequest predictRequest =
                                PredictRequest.newBuilder()
                                        .setName(name.toString())
                                        .setPayload(payload)
                                        .putParams(
                                                "score_threshold", "0.0") // [0.0-1.0] Only produce results higher than this value
                                        .build();

                        PredictResponse response = client.predict(predictRequest);
                        Optional<AnnotationPayload> typeOp = response.getPayloadList().stream().max(Comparator.comparingDouble(annotationPayload -> annotationPayload.getClassification().getScore()));
                        if (typeOp.isPresent()) {
                            type = typeOp.get().getDisplayName();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Tile brick = Tile.builder()
                        .name(title)
                        .photo(imgBytes)
                        .type(type)
                        .price(Double.parseDouble(price))
                        .build();

                service.createTile(brick);
            });
            Elements a = doc.select(".next.i-next");
            if (a.size() > 0) {
                url = a.attr("href");
            } else {
                break;
            }
        }
        return ResponseEntity.ok().build();
    }

    //    @Scheduled(cron = "0 0 * * 0")
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

                if (!price.isBlank()) {
                    Pattern pattern = Pattern.compile("\\d*,\\d{2}");
                    Matcher matcher = pattern.matcher(price);
                    matcher.find();
                    price = matcher.group();
                    price = price.replace(",", ".");

                    byte[] imgBytes = null;
                    try {
                        URL photoUrl = new URL(imgUrl + imgUrlLazy);
                        try (InputStream stream = photoUrl.openStream()) {
                            imgBytes = stream.readAllBytes();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Tile brick = Tile.builder()
                            .name(title)
                            .photo(imgBytes)
//                        .type(type)
                            .price(Double.parseDouble(price))
                            .build();
                    service.createTile(brick);
                }
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
