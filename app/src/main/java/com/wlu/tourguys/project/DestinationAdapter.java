package com.wlu.tourguys.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    private final List<Destination> destinationList;
    private final Context context;
    String userEmail, userPhone;


    public DestinationAdapter(List<Destination> destinationList, Context context) {
        this.destinationList = destinationList;
        this.context = context;
    }

    // New method for testing
    public List<Destination> getCurrentList() {
        return new ArrayList<>(destinationList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destination destination = destinationList.get(position);

        // Updated to use valid getters from Destination class
        holder.name.setText(destination.getUserName());
        holder.dates.setText("Dates: " + destination.getStartDate() + " to " + destination.getEndDate());
        holder.count.setText("Count: " + destination.getNumPeople());
        holder.location.setText(destination.getDestinationCity());
        holder.country.setText(destination.getDestinationCountry());
        userEmail = destination.getUserEmail();
        userPhone = destination.getUserPhone();


        // Handle the "Info" button click
        holder.infoButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailsActivity.class);

            // Pass data to the next activity
            intent.putExtra("destination", destination);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, dates, count, location, country;
        Button infoButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            dates = itemView.findViewById(R.id.dates);
            count = itemView.findViewById(R.id.count);
            location = itemView.findViewById(R.id.location);
            country = itemView.findViewById(R.id.country);
            infoButton = itemView.findViewById(R.id.info_button);


        }
    }

    public void updateData(List<Destination> newDestinationList) {
        this.destinationList.clear();
        this.destinationList.addAll(newDestinationList);
        notifyDataSetChanged();
    }
}