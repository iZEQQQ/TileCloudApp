package com.example.demo.controller;

import com.google.cloud.automl.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class ClassificationController {

    @GetMapping("/class")
    public ResponseEntity<Void> test() throws IOException {

        try (PredictionServiceClient client = PredictionServiceClient.create()) {
            ModelName name = ModelName.of("217603025665", "us-central1", "ICN616089350391726080");
            ByteString content = ByteString.copyFrom(Files.readAllBytes(Paths.get("/home/jgorny/IdeaProjects/demo/src/main/resources/jpg/cegielka0.jpg")));
            Image image = Image.newBuilder().setImageBytes(content).build();
            ExamplePayload payload = ExamplePayload.newBuilder().setImage(image).build();
            PredictRequest predictRequest =
                    PredictRequest.newBuilder()
                            .setName(name.toString())
                            .setPayload(payload)
                            .putParams(
                                    "score_threshold", "0.8") // [0.0-1.0] Only produce results higher than this value
                            .build();

            PredictResponse response = client.predict(predictRequest);

            for (AnnotationPayload annotationPayload : response.getPayloadList()) {
                System.out.format("Predicted class name: %s\n", annotationPayload.getDisplayName());
                System.out.format(
                        "Predicted class score: %.2f\n", annotationPayload.getClassification().getScore());
            }
        }
        return ResponseEntity.ok().build();

    }

}
