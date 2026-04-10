package com.example.baitap05;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {
    private String movieTitle, theaterName, showtime, seats;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        movieTitle = getIntent().getStringExtra("movieTitle");
        theaterName = getIntent().getStringExtra("theaterName");
        showtime = getIntent().getStringExtra("showtime");
        seats = getIntent().getStringExtra("seats");
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0);

        ((TextView)findViewById(R.id.tvConfMovie)).setText("Phim: " + movieTitle);
        ((TextView)findViewById(R.id.tvConfTheater)).setText("Rạp: " + theaterName);
        ((TextView)findViewById(R.id.tvConfTime)).setText("Suất chiếu: " + showtime);
        ((TextView)findViewById(R.id.tvConfSeats)).setText("Ghế: " + seats);
        ((TextView)findViewById(R.id.tvConfTotal)).setText("TỔNG TIỀN: " + String.format("%,.0f", totalPrice) + " VNĐ");

        findViewById(R.id.btnFinalConfirm).setOnClickListener(v -> saveBooking());
    }

    private void saveBooking() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();

        Map<String, Object> booking = new HashMap<>();
        booking.put("userId", userId);
        booking.put("movieTitle", movieTitle);
        booking.put("theaterName", theaterName);
        booking.put("showtime", showtime);
        booking.put("seats", seats);
        booking.put("totalPrice", totalPrice);
        booking.put("timestamp", System.currentTimeMillis());

        db.collection("bookings").add(booking)
                .addOnSuccessListener(documentReference -> {
                    sendLocalNotification();
                    Toast.makeText(this, "ĐẶT VÉ THÀNH CÔNG!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void sendLocalNotification() {
        String channelId = "booking_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Booking Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Đặt vé thành công!")
                .setContentText("Bạn đã đặt vé xem " + movieTitle + " tại " + theaterName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }
}
