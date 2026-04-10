package com.example.baitap05.models;

public class Showtime {
    private String id;
    private String movieId;
    private String theaterId;
    private String theaterName;
    private String time; // e.g., "2024-10-20 18:00"

    public Showtime() {}

    public Showtime(String id, String movieId, String theaterId, String theaterName, String time) {
        this.id = id;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.time = time;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }
    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    @Override
    public String toString() {
        return theaterName + " - " + time;
    }
}
