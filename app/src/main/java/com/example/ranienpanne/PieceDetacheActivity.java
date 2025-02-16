package com.example.ranienpanne;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PieceDetacheActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PieceDetacheAdapter pieceDetacheAdapter;
    private List<PieceDetache> pieceDetacheList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece_detache);

        // Initialize RecyclerView and LayoutManager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list and adapter
        pieceDetacheList = new ArrayList<>();
        pieceDetacheAdapter = new PieceDetacheAdapter(pieceDetacheList, this);
        recyclerView.setAdapter(pieceDetacheAdapter);

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch "Piece Detache" users
        databaseReference.orderByChild("type").equalTo("Piece Detache").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pieceDetacheList.clear();

                if (!dataSnapshot.exists()) {
                    Log.d("PieceDetacheActivity", "No data found in database.");
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String position = snapshot.child("position").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);

                    if (position == null) position = "N/A";
                    if (description == null) description = "No description available";

                    if (username != null && phone != null) {
                        PieceDetache piece = new PieceDetache(username, phone, position, description);
                        pieceDetacheList.add(piece);
                    } else {
                        Log.d("PieceDetacheActivity", "Incomplete data for user: " + snapshot.getKey());
                    }
                }

                pieceDetacheAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PieceDetacheActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
