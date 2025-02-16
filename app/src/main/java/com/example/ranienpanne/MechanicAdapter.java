package com.example.ranienpanne;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MechanicAdapter extends RecyclerView.Adapter<MechanicAdapter.MechanicViewHolder> {

    private List<Mechanic> mechanicList;

    public MechanicAdapter(List<Mechanic> mechanicList) {
        this.mechanicList = mechanicList;
    }

    @NonNull
    @Override
    public MechanicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the mechanic item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mechanic, parent, false);
        return new MechanicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MechanicViewHolder holder, int position) {
        // Get the mechanic at the current position
        Mechanic mechanic = mechanicList.get(position);

        // Set the mechanic details in the view holder
        holder.tvUsername.setText(mechanic.getUsername());
        holder.tvPhone.setText(mechanic.getPhone());
        holder.tvPosition.setText(mechanic.getPosition());
        holder.tvDescription.setText(mechanic.getDescription());

        // Set click listener for the phone button
        holder.tvPhone.setOnClickListener(v -> {
            // Handle phone number click, initiate a call
            String phoneNumber = mechanic.getPhone();
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            holder.itemView.getContext().startActivity(callIntent);
        });

        // Set click listener for the Map button
        holder.btnMap.setOnClickListener(v -> {
            // Handle button click, e.g., navigating to a map with the mechanic's location
            String geoUri = "geo:" + mechanic.getPosition();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            intent.setPackage("com.google.android.apps.maps");
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mechanicList.size();
    }

    public class MechanicViewHolder extends RecyclerView.ViewHolder {
        // Declare the TextViews for each mechanic's details
        TextView tvUsername, tvPhone, tvPosition, tvDescription;
        Button btnMap;  // Button for showing location on the map

        public MechanicViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews and Button
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnMap = itemView.findViewById(R.id.btnMap);  // Reference to the Map button
        }
    }
}
