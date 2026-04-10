package com.example.baitap05.models;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private String imageUrl;
    private String description;
    private int duration; // In minutes
    private double price;

    public Movie() {}

    public Movie(String id, String title, String genre, String imageUrl, String description, int duration, double price) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.description = description;
        this.duration = duration;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
