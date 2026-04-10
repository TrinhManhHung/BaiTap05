package com.example.baitap05;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitap05.models.Movie;
import com.example.baitap05.models.Ticket;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashSet;
import java.util.Set;

public class BookingActivity extends AppCompatActivity {
    private String movieId, movieTitle, showtime, theaterName;
    private GridView gvSeats;
    private Button btnConfirm;
    private FirebaseFirestore db;
    private Set<String> bookedSeats = new HashSet<>();
    private Set<String> selectedSeats = new HashSet<>();
    private double moviePrice = 80000; // Mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance();
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");
        showtime = getIntent().getStringExtra("showtime");
        theaterName = getIntent().getStringExtra("theaterName");

        // Lấy giá phim
        db.collection("movies").document(movieId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                moviePrice = doc.getDouble("price") != null ? doc.getDouble("price") : 80000;
            }
        });

        ((TextView)findViewById(R.id.tvBookingMovieTitle)).setText(movieTitle);
        ((TextView)findViewById(R.id.tvBookingShowtimeInfo)).setText(theaterName + " | " + showtime);

        gvSeats = findViewById(R.id.gvSeats);
        btnConfirm = findViewById(R.id.btnConfirmBooking);

        loadBookedSeats();

        btnConfirm.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ghế", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Chuyển sang màn hình Xác nhận
            Intent intent = new Intent(this, ConfirmationActivity.class);
            intent.putExtra("movieTitle", movieTitle);
            intent.putExtra("theaterName", theaterName);
            intent.putExtra("showtime", showtime);
            intent.putExtra("seats", selectedSeats.toString());
            intent.putExtra("totalPrice", selectedSeats.size() * moviePrice);
            startActivity(intent);
        });
    }

    private void loadBookedSeats() {
        db.collection("tickets")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("showtime", showtime)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookedSeats.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            bookedSeats.add(doc.getString("seat"));
                        }
                        gvSeats.setAdapter(new SeatAdapter());
                    }
                });
    }

    class SeatAdapter extends BaseAdapter {
        private String[] seats = {"A1", "A2", "A3", "A4", "A5", "B1", "B2", "B3", "B4", "B5", "C1", "C2", "C3", "C4", "C5", "D1", "D2", "D3", "D4", "D5"};

        @Override public int getCount() { return seats.length; }
        @Override public Object getItem(int i) { return seats[i]; }
        @Override public long getItemId(int i) { return i; }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) view = LayoutInflater.from(BookingActivity.this).inflate(R.layout.item_seat, viewGroup, false);
            CheckBox cb = view.findViewById(R.id.cbSeat);
            String seatName = seats[i];
            cb.setText(seatName);
            cb.setOnCheckedChangeListener(null); 
            
            if (bookedSeats.contains(seatName)) {
                cb.setEnabled(false);
                cb.setChecked(false);
            } else {
                cb.setEnabled(true);
                cb.setChecked(selectedSeats.contains(seatName));
                cb.setOnCheckedChangeListener((bv, isChecked) -> {
                    if (isChecked) selectedSeats.add(seatName);
                    else selectedSeats.remove(seatName);
                });
            }
            return view;
        }
    }
}
