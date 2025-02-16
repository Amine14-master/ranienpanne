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

public class DepannageAdapter extends RecyclerView.Adapter<DepannageAdapter.DepannageViewHolder> {

    private List<Depannage> depannageList;
    private OnCallButtonClickListener callButtonClickListener;

    public interface OnCallButtonClickListener {
        void onCallButtonClick(String phoneNumber);
    }

    public DepannageAdapter(List<Depannage> depannageList, OnCallButtonClickListener listener) {
        this.depannageList = depannageList;
        this.callButtonClickListener = listener;
    }

    @NonNull
    @Override
    public DepannageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_depannage, parent, false);
        return new DepannageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepannageViewHolder holder, int position) {
        Depannage depannage = depannageList.get(position);
        holder.usernameTextView.setText(depannage.getUsername());
        holder.positionTextView.setText(depannage.getPosition());
        holder.descriptionTextView.setText(depannage.getDescription());

        holder.callButton.setOnClickListener(v -> {
            if (callButtonClickListener != null) {
                callButtonClickListener.onCallButtonClick(depannage.getPhone());
            }
        });
    }

    @Override
    public int getItemCount() {
        return depannageList.size();
    }

    public static class DepannageViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView positionTextView;
        TextView descriptionTextView;
        Button callButton;

        public DepannageViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            positionTextView = itemView.findViewById(R.id.position);
            descriptionTextView = itemView.findViewById(R.id.description);
            callButton = itemView.findViewById(R.id.callButton);
        }
    }
}
