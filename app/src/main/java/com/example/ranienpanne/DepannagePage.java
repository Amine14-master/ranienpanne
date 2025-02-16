package com.example.ranienpanne;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DepannagePage extends AppCompatActivity {

    private EditText etUsername, etPhoneNumber, etDescription, etPosition;
    private Button btnSaveChanges, btnLogout; // Added logout button
    private DatabaseReference database;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depannage_page);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etDescription = findViewById(R.id.etDescription);
        etPosition = findViewById(R.id.etPosition);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnLogout = findViewById(R.id.btnLogout); // Initialize logout button

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Define the location listener to track position changes
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String position = location.getLatitude() + ", " + location.getLongitude();
                etPosition.setText(position);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Toast.makeText(DepannagePage.this, "Please enable GPS", Toast.LENGTH_SHORT).show();
            }
        };

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        // Button to save changes
        btnSaveChanges.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String phone = etPhoneNumber.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String position = etPosition.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(position)) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            String userKey = phone + "_Depannage"; // Assuming unique key is phone + type
            Depannage depannage = new Depannage(username, phone, position, description);

            database.child(userKey).setValue(depannage).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(DepannagePage.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DepannagePage.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Logout button functionality
        btnLogout.setOnClickListener(v -> {
            // Clear user session from SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear(); // Remove all user data
            editor.apply();

            // Navigate back to LoginActivity
            Intent intent = new Intent(DepannagePage.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            Toast.makeText(DepannagePage.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove location updates when activity is destroyed to save battery
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
