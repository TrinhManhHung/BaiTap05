package com.example.baitap05.models;

public class Ticket {
    private String id;
    private String userId;
    private String movieId;
    private String movieTitle;
    private String showtime;
    private String seat;

    public Ticket() {}

    public Ticket(String id, String userId, String movieId, String movieTitle, String showtime, String seat) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.showtime = showtime;
        this.seat = seat;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getShowtime() { return showtime; }
    public void setShowtime(String showtime) { this.showtime = showtime; }
    public String getSeat() { return seat; }
    public void setSeat(String seat) { this.seat = seat; }
}
