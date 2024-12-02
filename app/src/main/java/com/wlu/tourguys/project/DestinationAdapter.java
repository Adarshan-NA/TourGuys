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

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    private final List<Destination> destinationList;
    private final Context context;

    public DestinationAdapter(List<Destination> destinationList, Context context) {
        this.destinationList = destinationList;
        this.context = context;
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
        holder.name.setText("Location: " + destination.getDestinationCity() + ", " + destination.getDestinationCountry());
        holder.dates.setText("Dates: " + destination.getStartDate() + " to " + destination.getEndDate());
        holder.count.setText("Count: " + destination.getNumPeople());
        holder.location.setText(destination.getDestinationCity());
        holder.country.setText(destination.getDestinationCountry());

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