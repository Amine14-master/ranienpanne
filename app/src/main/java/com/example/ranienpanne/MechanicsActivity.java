package com.example.ranienpanne;

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

public class MechanicsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MechanicAdapter mechanicAdapter;
    private List<Mechanic> mechanicList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanics);

        // Initialize RecyclerView and LayoutManager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list and adapter for mechanics
        mechanicList = new ArrayList<>();
        mechanicAdapter = new MechanicAdapter(mechanicList);
        recyclerView.setAdapter(mechanicAdapter);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch all users data from Firebase (no filtering by type)
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mechanicList.clear();  // Clear the list to avoid duplicate entries

                // Check if data exists
                if (!dataSnapshot.exists()) {
                    Log.d("MechanicsActivity", "No data found in database.");
                    return;
                }

                // Iterate through the retrieved data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String position = snapshot.child("position").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);

                    // If any field is missing, set default values
                    if (position == null) {
                        position = "N/A";  // Default value
                    }
                    if (description == null) {
                        description = "No description available";  // Default value
                    }

                    // Check if username and phone are not null (since they are essential)
                    if (username != null && phone != null) {
                        Mechanic mechanic = new Mechanic(username, phone, position, description);
                        mechanicList.add(mechanic);
                        Log.d("MechanicsActivity", "Added mechanic: " + username);
                    } else {
                        // Log missing user data for debugging
                        Log.d("MechanicsActivity", "Incomplete data for user: " + snapshot.getKey());
                    }
                }

                // Notify the adapter that data has changed and RecyclerView needs to update
                mechanicAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error gracefully
                Toast.makeText(MechanicsActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
