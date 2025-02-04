package com.souravmodak.feedtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FeedService {
    @Autowired
    FeedRepository feedRepository;


    private static final String CACHE_KEY = "posts";

    public Page<Feed> getAtDistanceGreaterThan(double distance, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("distance").ascending());
        return feedRepository.findAllByDistanceGreaterThan(distance, pageable);
    }
    public Page<Feed> getAtDistanceLessThan(double distance, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("distance").descending());
        return feedRepository.findAllByDistanceLessThan(distance, pageable);
    }
    public Page<Feed> getAtDistance(double distance, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("distance").ascending());
        return feedRepository.findByDistance(distance, pageable);
    }
}
