package com.souravmodak.feedtest;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Feed {
    @Id
    Long id;
    int age;
    String name;
    double distance;
    String imageUrl;

    public Feed(Long id, int age, String name, double distance, String imageUrl) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.distance = distance;
        this.imageUrl = imageUrl;
    }

    public Feed() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
