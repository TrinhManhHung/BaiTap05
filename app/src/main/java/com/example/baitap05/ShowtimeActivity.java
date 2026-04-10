package com.example.baitap05;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitap05.adapters.TheaterAdapter;
import com.example.baitap05.models.Showtime;
import com.example.baitap05.models.Theater;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowtimeActivity extends AppCompatActivity {
    private String movieId, movieTitle;
    private RecyclerView rvTheaters;
    private TheaterAdapter theaterAdapter;
    private List<Theater> theaterList;
    private Map<String, List<Showtime>> theaterShowtimesMap;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime);

        db = FirebaseFirestore.getInstance();
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");

        ((TextView)findViewById(R.id.tvShowtimeMovieTitle)).setText(movieTitle);

        rvTheaters = findViewById(R.id.rvTheaters);
        theaterList = new ArrayList<>();
        theaterShowtimesMap = new HashMap<>();
        theaterAdapter = new TheaterAdapter(theaterList, theaterShowtimesMap, this, movieTitle);
        rvTheaters.setLayoutManager(new LinearLayoutManager(this));
        rvTheaters.setAdapter(theaterAdapter);

        loadTheatersAndShowtimes();
    }

    private void loadTheatersAndShowtimes() {
        // 1. Tải tất cả rạp
        db.collection("theaters").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                theaterList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    theaterList.add(doc.toObject(Theater.class));
                }
                
                // 2. Tải tất cả suất chiếu của phim này
                db.collection("showtimes")
                        .whereEqualTo("movieId", movieId)
                        .get()
                        .addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                theaterShowtimesMap.clear();
                                for (QueryDocumentSnapshot doc : task2.getResult()) {
                                    Showtime s = doc.toObject(Showtime.class);
                                    if (!theaterShowtimesMap.containsKey(s.getTheaterId())) {
                                        theaterShowtimesMap.put(s.getTheaterId(), new ArrayList<>());
                                    }
                                    theaterShowtimesMap.get(s.getTheaterId()).add(s);
                                }
                                theaterAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });
    }
}
