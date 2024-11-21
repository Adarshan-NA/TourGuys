package com.wlu.tourguys.project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private final List<Destination> destinations;

    public DestinationAdapter(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        TextView name, dates, count, location, country;
        Button infoButton;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            dates = itemView.findViewById(R.id.dates);
            count = itemView.findViewById(R.id.count);
            location = itemView.findViewById(R.id.location);
            country = itemView.findViewById(R.id.country);
            infoButton = itemView.findViewById(R.id.info_button);
        }
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        // Get the destination at the current position
        Destination destination = destinations.get(position);

        // Set the text for the views in the ViewHolder
        holder.name.setText("Name: " + destination.getName());
        holder.dates.setText("Dates: " + destination.getDates());
        holder.count.setText("Count: " + destination.getCount());
        holder.location.setText(destination.getLocation());
        holder.country.setText(destination.getCountry());

        // Calculate the number of days from the dates
        String dates = destination.getDates(); // Assuming dates are in the format "December 15 - December 20"
        long daysCount = calculateDays(dates);

        // Set up the "Info" button click listener
        holder.infoButton.setOnClickListener(view -> {
            // Create an intent to navigate to DetailsActivity
            Intent intent = new Intent(view.getContext(), DetailsActivity.class);

            // Pass the destination details to the DetailsActivity
            intent.putExtra("DESTINATION_NAME", destination.getLocation());
            intent.putExtra("DESTINATION_LOCATION", destination.getCountry());
            intent.putExtra("DAYS_COUNT", String.valueOf(daysCount)); // Pass the calculated days count
            intent.putExtra("TRAVELER_NAME", destination.getName()); // Assuming traveler name is destination name
            intent.putExtra("TRAVEL_DATES", destination.getDates());
            intent.putExtra("SOURCE", destination.getCountry()); // Assuming "country" is the source
            intent.putExtra("TOTAL_PEOPLE", destination.getCount());
            intent.putExtra("MALE_COUNT", "3"); // Placeholder value; replace if you have a field in the Destination class
            intent.putExtra("FEMALE_COUNT", "4"); // Placeholder value; replace if you have a field in the Destination class
            intent.putExtra("BUDGET", "$360"); // Placeholder value; replace if you have a field in the Destination class

            // Start the DetailsActivity
            view.getContext().startActivity(intent);
        });
    }

    // Helper method to calculate the number of days between two dates
    private long calculateDays(String dates) {
        try {
            // Split the dates string into start and end dates
            String[] dateParts = dates.split(" - ");
            if (dateParts.length == 2) {
                String startDateStr = dateParts[0].trim(); // Example: "December 15"
                String endDateStr = dateParts[1].trim();   // Example: "December 20"

                // Add the year to the dates (assuming the current year)
                int currentYear = java.time.Year.now().getValue();
                startDateStr += " " + currentYear;
                endDateStr += " " + currentYear;

                // Define a date formatter to parse the dates
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM d yyyy");

                // Parse the dates
                java.time.LocalDate startDate = java.time.LocalDate.parse(startDateStr, formatter);
                java.time.LocalDate endDate = java.time.LocalDate.parse(endDateStr, formatter);

                // Calculate the difference in days
                return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if an error occurs
    }



    @Override
    public int getItemCount() {
        return destinations.size();
    }
}
