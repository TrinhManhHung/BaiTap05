package com.example.baitap05.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitap05.BookingActivity;
import com.example.baitap05.R;
import com.example.baitap05.models.Showtime;

import java.util.List;

public class ShowtimeChipAdapter extends RecyclerView.Adapter<ShowtimeChipAdapter.ChipViewHolder> {
    private List<Showtime> showtimes;
    private Context context;
    private String movieTitle;

    public ShowtimeChipAdapter(List<Showtime> showtimes, Context context, String movieTitle) {
        this.showtimes = showtimes;
        this.context = context;
        this.movieTitle = movieTitle;
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime_chip, parent, false);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        Showtime s = showtimes.get(position);
        holder.tvChipTime.setText(s.getTime());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("movieId", s.getMovieId());
            intent.putExtra("movieTitle", movieTitle);
            intent.putExtra("showtimeId", s.getId());
            intent.putExtra("showtime", s.getTime());
            intent.putExtra("theaterName", s.getTheaterName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return showtimes.size();
    }

    static class ChipViewHolder extends RecyclerView.ViewHolder {
        TextView tvChipTime;
        public ChipViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChipTime = itemView.findViewById(R.id.tvChipTime);
        }
    }
}
