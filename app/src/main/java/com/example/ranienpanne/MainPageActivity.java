package com.example.ranienpanne;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainPageActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String USER_ID = "+213549424053_Simple"; // Replace with dynamically fetched user ID
    private DatabaseReference databaseReference;
    private FusedLocationProviderClient fusedLocationClient;
    private Switch switchPanne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);

        // Initialize Firebase and Location Client
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Main Services
        ImageView imgDepannage = findViewById(R.id.imgDepannage);
        ImageView imgMecanicien = findViewById(R.id.imgMecanicien);

        // Secondary Services
        ImageView imgPieceDetache = findViewById(R.id.imgPieceDetache);
        ImageView imgAskAI = findViewById(R.id.imgAskAI);

        // Logout Button
        Button btnLogout = findViewById(R.id.btnLogout);

        // "Panne" Switch
        switchPanne = findViewById(R.id.switchPanne);
        switchPanne.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                requestAndSaveLocation();
            } else {
                updatePanneStatus(false, 0.0, 0.0);
                Toast.makeText(this, "Panne disabled. Help request removed.", Toast.LENGTH_SHORT).show();
            }
        });

        // Map Button
        Button btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(v -> {
            Toast.makeText(this, "Opening map...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainPageActivity.this, MapActivity.class));
        });

        // Click Listeners for Main Services
        imgDepannage.setOnClickListener(v -> {
            Toast.makeText(this, "Dépannage selected", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainPageActivity.this, DepannageActivity.class));
        });

        imgMecanicien.setOnClickListener(v -> {
            Toast.makeText(this, "Mécanicien selected", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainPageActivity.this, MechanicsActivity.class));
        });

        imgPieceDetache.setOnClickListener(v -> {
            Toast.makeText(this, "Pièce Détachée selected", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainPageActivity.this, PieceDetacheActivity.class));
        });

        imgAskAI.setOnClickListener(v -> {
            Toast.makeText(this, "Ask AI selected", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainPageActivity.this, AskAIActivity.class));
        });

        // Logout Button Logic
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout Confirmation")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        navigateToLoginActivity();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(MainPageActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void requestAndSaveLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        updatePanneStatus(true, latitude, longitude);
                        Toast.makeText(this, "Panne enabled. Location saved.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to get location. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void updatePanneStatus(boolean isInPanne, double latitude, double longitude) {
        databaseReference.child(USER_ID).child("inPanne").setValue(isInPanne);
        if (isInPanne) {
            databaseReference.child(USER_ID).child("latitude").setValue(latitude);
            databaseReference.child(USER_ID).child("longitude").setValue(longitude);
        } else {
            databaseReference.child(USER_ID).child("latitude").removeValue();
            databaseReference.child(USER_ID).child("longitude").removeValue();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestAndSaveLocation();
            } else {
                Toast.makeText(this, "Location permission is required to enable Panne.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
