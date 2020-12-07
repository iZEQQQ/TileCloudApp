package com.example.demo.controller;

import com.example.demo.entity.Tile;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.DominantColorsAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.ImageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class VisionController {

    private final CloudVisionTemplate cloudVisionTemplate;
    private final ResourceLoader resourceLoader;

    @Autowired
    public VisionController(CloudVisionTemplate cloudVisionTemplate, ResourceLoader resourceLoader) {
        this.cloudVisionTemplate = cloudVisionTemplate;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/vision")
    public ResponseEntity<Map> sayHello() {

        Resource resource = resourceLoader.getResource("classpath:jpg/image.jpeg");

        AnnotateImageResponse imageResponse =
                cloudVisionTemplate.analyzeImage(resource, Feature.Type.IMAGE_PROPERTIES);

        ImageProperties propertiesAnnotation = imageResponse.
                getImagePropertiesAnnotation();

        DominantColorsAnnotation dominantColors = propertiesAnnotation.getDominantColors();

        Map<String, String> colorMap = new HashMap<>();

        dominantColors.getColorsList().forEach(colorInfo -> {
            colorMap.put(colorInfo.getColor().toString(), colorInfo.getPixelFraction() + "");
        });
//TODO  trzeba zrobic jakas autoryzacje z kluczem do vision oraz zablokowac dostep bo jest publiczne domyslnie
        return ResponseEntity.ok(colorMap);
    }


}
