package auca.ac.rw.food.delivery.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.rw.food.delivery.management.model.TestModel;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @PostMapping
    public ResponseEntity<String> testPost(@RequestBody TestModel model) {
        return ResponseEntity.ok("Received: " + model.getMessage());
    }
}