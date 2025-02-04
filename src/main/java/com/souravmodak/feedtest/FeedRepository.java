package com.souravmodak.feedtest;

import com.souravmodak.feedtest.models.entities.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findAllByDistanceGreaterThan(double distance, Pageable pageable);
    Page<Feed> findAllByDistanceLessThan(double distance, Pageable pageable);
    Page<Feed> findByDistance(double distance, Pageable pageable);
}
