package com.example.demo.controller;

import com.example.demo.tiles.repository.model.Tile;
import com.example.demo.tiles.service.TileService;
import com.google.cloud.automl.v1.*;
import com.google.protobuf.ByteString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jgorny
 * Its a two simple crowler service for providing data from two websites:
 * - www.eplytki.pl
 * - www.domus-sklep.pl
 *
 * crowlers use jsoup to get the connection afterwards we get data from elements.
 * After that method changes photos to bytes so the sorage is easy and efficient.
 * Then using vision from google we get read the photo and type is automaticly assigned
 * to the tile type. Finally with builder design pattern generated from Lombok annotation
 * method is building object from parameters and TileService is creating a tile and saving it to the database.
 */


@Service
public class CrawlerService {

    private TileService service;

    @Autowired
    public CrawlerService(TileService service) {
        this.service = service;
    }


//     oraz dodawanie do bazy danych widok uzytkownika
//    TODO dodac po rejestracji mozliwosc oceniania
//    TODO dodac lokalizajce z ktorego sklepu zebrano

//    @PostConstruct
//    public void init() throws IOException {
//        scrapEplytki();
//        scrapDomus();
//    }


    @Scheduled(cron = "0 0 * * 0")
    public void scrapEplytki() throws IOException {
        String url = "https://www.eplytki.pl/plytki-lazienkowe.html?limit=72";
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
                        Optional<AnnotationPayload> typeOp = response.getPayloadList().stream()
                                .max(Comparator.comparingDouble(annotationPayload -> annotationPayload.getClassification().getScore()));
                        if (typeOp.isPresent()) {
                            type = typeOp.get().getDisplayName();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Tile tile = Tile.builder()
                        .name(title)
                        .photo(imgBytes)
                        .type(type)
                        .price(Double.parseDouble(price))
                        .build();

                service.createTile(tile);
            });
            Elements a = doc.select(".next.i-next");
            if (a.size() > 0) {
                url = a.attr("href");
            } else {
                break;
            }
        }
    }

    @Scheduled(cron = "0 0 * * 0")
    public void scrapDomus() throws IOException {
        String url = "https://domus-sklep.pl/1107-wszystkie-plytki";
//        TODO przestawic na 4 strony finalnie
        for (int i = 0; i < 1; i++) {
                    Document doc = Jsoup.connect(url).get();
            Elements items = doc.select(".pro_outer_box");
//            items.forEach(element -> {
//            TODO finalnie odblokowac by nie pobieralo tylko 4 itemow a wszystkie z strony
            items.stream().limit(4).forEach(element -> {
                Elements img = element.select(".replace-2x.img-responsive.menu_pro_img");
                Elements imgLazy = element.select(".lazy-loading.replace-2x.img-responsive.front-image");
                String imgUrlLazy = imgLazy.attr("data-src");
                String titleLazy = imgLazy.attr("title");
                String title = img.attr("alt");
                String imgUrl = img.attr("src");
                Elements span = element.select(".price.product-price");
                String price = span.text();
                String type = null;

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
                    if(imgBytes != null){
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
                    Tile tile = Tile.builder()
                            .name(title+titleLazy)
                            .photo(imgBytes)
                            .type(type)
                            .price(Double.parseDouble(price))
                            .build();
                    service.createTile(tile);
                }
            });
            Elements li = doc.select(".pagination_next > a");

            if (li.size() > 0) {
                url = "https://domus-sklep.pl" + li.attr("href");
            } else {
                break;
            }
        }
    }


}
