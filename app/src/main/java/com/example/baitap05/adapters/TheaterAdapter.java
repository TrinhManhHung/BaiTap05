package com.example.baitap05.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitap05.R;
import com.example.baitap05.models.Showtime;
import com.example.baitap05.models.Theater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder> {
    private List<Theater> theaterList;
    private Map<String, List<Showtime>> theaterShowtimesMap;
    private Context context;
    private String movieTitle;

    public TheaterAdapter(List<Theater> theaterList, Map<String, List<Showtime>> theaterShowtimesMap, Context context, String movieTitle) {
        this.theaterList = theaterList;
        this.theaterShowtimesMap = theaterShowtimesMap;
        this.context = context;
        this.movieTitle = movieTitle;
    }

    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theater, parent, false);
        return new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
        Theater theater = theaterList.get(position);
        holder.tvName.setText(theater.getName());
        holder.tvAddress.setText(theater.getAddress());

        List<Showtime> showtimes = theaterShowtimesMap.get(theater.getId());
        if (showtimes == null) showtimes = new ArrayList<>();

        ShowtimeChipAdapter chipAdapter = new ShowtimeChipAdapter(showtimes, context, movieTitle);
        holder.rvShowtimes.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvShowtimes.setAdapter(chipAdapter);
    }

    @Override
    public int getItemCount() {
        return theaterList.size();
    }

    static class TheaterViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress;
        RecyclerView rvShowtimes;
        public TheaterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemTheaterName);
            tvAddress = itemView.findViewById(R.id.tvItemTheaterAddress);
            rvShowtimes = itemView.findViewById(R.id.rvItemShowtimes);
        }
    }
}
