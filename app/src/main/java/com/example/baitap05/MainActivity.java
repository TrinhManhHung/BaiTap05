package com.example.baitap05;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitap05.adapters.MovieAdapter;
import com.example.baitap05.models.Movie;
import com.example.baitap05.models.Showtime;
import com.example.baitap05.models.Theater;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        rvMovies = findViewById(R.id.rvMovies);
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(movieList, this);

        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(movieAdapter);

        loadMovies();
    }

    private void loadMovies() {
        db.collection("movies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        movieList.clear();
                        if (task.getResult().isEmpty()) {
                            addSampleData();
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Movie movie = document.toObject(Movie.class);
                                movie.setId(document.getId());
                                movieList.add(movie);
                            }
                            movieAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(this, "Lỗi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSampleData() {
        Theater t1 = new Theater("t1", "CGV Vincom", "Quận 1, HCM");
        Theater t2 = new Theater("t2", "Lotte Cinema", "Quận 7, HCM");
        db.collection("theaters").document(t1.getId()).set(t1);
        db.collection("theaters").document(t2.getId()).set(t2);

        List<Movie> samples = new ArrayList<>();
        samples.add(new Movie("m1", "Dune: Part Two", "Sci-Fi", "https://image.tmdb.org/t/p/w500/8b8R8lS87vPk9upUqaS7pA6vB8s.jpg", "Epic sci-fi.", 166, 95000));
        samples.add(new Movie("m2", "Oppenheimer", "Biography", "https://image.tmdb.org/t/p/w500/8GxvA0zYonesbbS7XUqzXgu7sF9.jpg", "Atomic bomb story.", 180, 85000));
        samples.add(new Movie("m3", "Spider-Man", "Action", "", "Spider hero.", 140, 75000));
        
        for (Movie m : samples) {
            db.collection("movies").document(m.getId()).set(m).addOnSuccessListener(aVoid -> {
                addShowtimesForMovie(m.getId());
            });
        }
        
        Toast.makeText(this, "Khởi tạo dữ liệu mẫu...", Toast.LENGTH_SHORT).show();
        rvMovies.postDelayed(this::loadMovies, 2000);
    }

    private void addShowtimesForMovie(String movieId) {
        db.collection("showtimes").add(new Showtime(null, movieId, "t1", "CGV Vincom", "18:00 - Hôm nay"));
        db.collection("showtimes").add(new Showtime(null, movieId, "t2", "Lotte Cinema", "20:30 - Hôm nay"));
    }
}
