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
public class DepannageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DepannageAdapter depannageAdapter;
    private List<Depannage> depannageList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depannage);

        // Initialize RecyclerView and LayoutManager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list and adapter for dépannage
        depannageList = new ArrayList<>();
        depannageAdapter = new DepannageAdapter(depannageList, new DepannageAdapter.OnCallButtonClickListener() {
            @Override
            public void onCallButtonClick(String phoneNumber) {
                // Handle call button click
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });
        recyclerView.setAdapter(depannageAdapter);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch "Dépannage" users data from Firebase
        databaseReference.orderByChild("type").equalTo("Dépannage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                depannageList.clear();  // Clear the list to avoid duplicate entries

                // Check if data exists
                if (!dataSnapshot.exists()) {
                    Log.d("DepannageActivity", "No data found in database.");
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
                        Depannage depannage = new Depannage(username, phone, position, description, "Dépannage");
                        depannageList.add(depannage);
                        Log.d("DepannageActivity", "Added depannage: " + username);
                    } else {
                        // Log missing user data for debugging
                        Log.d("DepannageActivity", "Incomplete data for user: " + snapshot.getKey());
                    }
                }

                // Notify the adapter that data has changed and RecyclerView needs to update
                depannageAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error gracefully
                Toast.makeText(DepannageActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
