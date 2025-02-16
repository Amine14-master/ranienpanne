package com.example.ranienpanne;

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

public class PieceDetachePage extends AppCompatActivity {

    private EditText etUsername, etPhoneNumber, etPosition, etDescription;
    private Button btnGetPosition, btnSaveChanges, btnLogout; // Logout button
    private DatabaseReference database;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece_detache_page);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPosition = findViewById(R.id.etPosition);
        etDescription = findViewById(R.id.etDescription);
        btnGetPosition = findViewById(R.id.btnGetPosition);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnLogout = findViewById(R.id.btnLogout); // Reference logout button

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Button to get the current position
        btnGetPosition.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    String position = location.getLatitude() + ", " + location.getLongitude();
                    etPosition.setText(position);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }

                @Override
                public void onProviderEnabled(@NonNull String provider) { }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    Toast.makeText(PieceDetachePage.this, "Please enable GPS", Toast.LENGTH_SHORT).show();
                }
            }, null);
        });

        // Button to save changes
        btnSaveChanges.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String phone = etPhoneNumber.getText().toString().trim();
            String position = etPosition.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(position)) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            String userKey = phone + "_PieceDetache"; // Assuming unique key is phone + type
            PieceDetache pieceDetache = new PieceDetache(username, phone, position, description);

            database.child(userKey).setValue(pieceDetache).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(PieceDetachePage.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PieceDetachePage.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Logout button functionality
        btnLogout.setOnClickListener(v -> {
            // Clear user session (if any, using SharedPreferences or other methods)
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear session data
            editor.apply();

            // Redirect to LoginActivity
            Intent intent = new Intent(PieceDetachePage.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close PieceDetachePage activity
        });
    }
}
