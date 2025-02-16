package com.example.ranienpanne;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Objects;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set OpenStreetMap Configuration
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_map);

        // Initialize MapView
        mapView = findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);

        // Center map on a default location (e.g., Algiers)
        mapView.getController().setZoom(10.0);
        mapView.getController().setCenter(new GeoPoint(36.7525, 3.04197)); // Latitude and longitude of Algiers

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch and display users in "Panne" mode
        fetchAndDisplayPanneUsers();
    }

    private void fetchAndDisplayPanneUsers() {
        databaseReference.orderByChild("inPanne").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mapView.getOverlays().clear(); // Clear existing markers

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Double latitude = userSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = userSnapshot.child("longitude").getValue(Double.class);
                    String username = userSnapshot.child("username").getValue(String.class);

                    if (latitude != null && longitude != null) {
                        // Add a marker for the user
                        addMarkerToMap(latitude, longitude, username);
                    }
                }
                Toast.makeText(MapActivity.this, "Map updated with Panne users.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MapActivity.this, "Failed to load users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addMarkerToMap(double latitude, double longitude, String username) {
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(latitude, longitude));
        marker.setTitle(username != null ? username : "Unknown User");
        marker.setSnippet("Needs assistance!");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
        mapView.invalidate(); // Refresh the map
    }
}
