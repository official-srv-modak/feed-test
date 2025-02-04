package com.souravmodak.feedtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping("/at-distance-less")
    public ResponseEntity<Page<Feed>> getAtDistanceLess(@RequestParam Double distance,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(feedService.getAtDistanceLessThan(distance, page, size));
    }

    @GetMapping("/at-distance-greater")
    public ResponseEntity<Page<Feed>> getAtDistanceMore(@RequestParam Double distance,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(feedService.getAtDistanceGreaterThan(distance, page, size));
    }

    @GetMapping("/at-distance")
    public ResponseEntity<Page<Feed>> getAtDistance(@RequestParam Double distance,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(feedService.getAtDistance(distance, page, size));
    }
}
