package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

@RestController
public class Controller {

    private final Bucket bucket;

    public Controller() {
        Bandwidth limitLonger = Bandwidth.classic(20, Refill.greedy(10, Duration.ofSeconds(2)));
        Bandwidth limitSpike = Bandwidth.classic(50, Refill.greedy(20, Duration.ofMinutes(10)));
        this.bucket = Bucket.builder()
                .addLimit(limitLonger)
                .addLimit(limitSpike)
                .build();
    }

    @GetMapping(value = "/hello")
    public ResponseEntity<String> hello() throws JsonProcessingException, InterruptedException {

        ObjectMapper objectMapper=new ObjectMapper();

        List<Entity> list=List.of(new Entity(1L,"Mark",30,"text"),
                new Entity(2L,"Mark",30,"text"),
                new Entity(3L,"Mark",31,"text"),
                new Entity(4L,"Mark",32,"text"),
                new Entity(5L,"Mark",33,"text"),
                new Entity(6L,"Mark",34,"text"));

        String responseBody=objectMapper.writeValueAsString(list);

        Thread.sleep(600);

        if (bucket.tryConsume(1))
            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
