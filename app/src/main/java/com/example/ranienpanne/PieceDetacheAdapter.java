package com.example.ranienpanne;

import android.content.Context;
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

public class PieceDetacheAdapter extends RecyclerView.Adapter<PieceDetacheAdapter.ViewHolder> {

    private List<PieceDetache> pieceDetacheList;
    private Context context;

    public PieceDetacheAdapter(List<PieceDetache> pieceDetacheList, Context context) {
        this.pieceDetacheList = pieceDetacheList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_piece_detache, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PieceDetache piece = pieceDetacheList.get(position);
        holder.usernameTextView.setText(piece.getUsername());
        holder.phoneTextView.setText(piece.getPhone());
        holder.positionTextView.setText(piece.getPosition());
        holder.descriptionTextView.setText(piece.getDescription());

        // Call button
        holder.callButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + piece.getPhone()));
            context.startActivity(callIntent);
        });

        // Map button
        holder.mapButton.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(piece.getPosition()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        });
    }

    @Override
    public int getItemCount() {
        return pieceDetacheList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView, phoneTextView, positionTextView, descriptionTextView;
        Button callButton, mapButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            callButton = itemView.findViewById(R.id.callButton);
            mapButton = itemView.findViewById(R.id.mapButton);
        }
    }
}
