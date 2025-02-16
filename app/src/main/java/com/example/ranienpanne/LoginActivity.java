package com.example.ranienpanne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhoneNumber, etUsername;
    private Spinner spinnerType;
    private Button btnLogin;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    private static final String PREFS_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etUsername = findViewById(R.id.etUsername);
        spinnerType = findViewById(R.id.spinnerType);
        btnLogin = findViewById(R.id.btnLogin);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        checkLoggedInUser();

        btnLogin.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();

            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(username) || TextUtils.isEmpty(type)) {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Format the phone number
            String formattedPhoneNumber = formatPhoneNumber(phoneNumber);
            if (formattedPhoneNumber == null) {
                Toast.makeText(LoginActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            String fullKey = formattedPhoneNumber + "_" + type;

            usersRef.child(fullKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String storedUsername = dataSnapshot.child("username").getValue(String.class);
                        if (username.equals(storedUsername)) {
                            // Save user info locally
                            saveUserInfo(formattedPhoneNumber, username, type);

                            // Redirect to the appropriate page
                            navigateToPage(type);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void checkLoggedInUser() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String phoneNumber = prefs.getString("phoneNumber", null);
        String username = prefs.getString("username", null);
        String type = prefs.getString("type", null);

        if (phoneNumber != null && username != null && type != null) {
            // User is already logged in, navigate to the appropriate page
            navigateToPage(type);
        } else {
            // If not logged in, continue with login flow
        }
    }

    private void saveUserInfo(String phoneNumber, String username, String type) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("username", username);
        editor.putString("type", type);
        editor.apply();
    }

    private void navigateToPage(String type) {
        Intent intent;
        switch (type) {
            case "Mécanicien":
                intent = new Intent(LoginActivity.this, MechanicPage.class);
                break;
            case "Dépannage":
                intent = new Intent(LoginActivity.this, DepannagePage.class);
                break;
            case "Pièce Détachée":
                intent = new Intent(LoginActivity.this, PieceDetachePage.class);
                break;
            default:
                intent = new Intent(LoginActivity.this, MainPageActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private String formatPhoneNumber(String phoneNumber) {
        // Simplified formatting logic for brevity
        return phoneNumber.replaceAll("\\s+", "");
    }
}
