package com.souravmodak.feedtest.api;

import com.souravmodak.feedtest.api.service.FeedService;
import com.souravmodak.feedtest.models.entities.Feed;
import com.souravmodak.feedtest.models.nonentities.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping("/at-distance-less")
    public BaseResponse<Page<Feed>> getAtDistanceLess(@RequestParam Double distance,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return new BaseResponse<>(feedService.getAtDistanceLessThan(distance, page, size), HttpStatus.OK, "Success");
    }

    @GetMapping("/at-distance-greater")
    public BaseResponse<Page<Feed>> getAtDistanceMore(@RequestParam Double distance,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return new BaseResponse<>(feedService.getAtDistanceGreaterThan(distance, page, size), HttpStatus.OK, "Success");
    }

    @GetMapping("/at-distance")
    public BaseResponse<Page<Feed>> getAtDistance(@RequestParam Double distance,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return new BaseResponse<>(feedService.getAtDistance(distance, page, size), HttpStatus.OK, "Success");
    }

    @GetMapping("/profile/{id}")
    public BaseResponse<Feed> getProfileById(@PathVariable("id") Long id) {
        return new BaseResponse<>(feedService.getProfile(id), HttpStatus.OK, "Success");
    }
}
