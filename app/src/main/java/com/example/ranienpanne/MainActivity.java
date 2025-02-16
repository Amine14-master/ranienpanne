package com.example.ranienpanne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "UserSession";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check login status
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // If the user is logged in, navigate to MainPageActivity
            navigateToMainPageActivity();
        } else {
            // Set the content view for the login screen
            setContentView(R.layout.activity_main);

            Button btnLogin = findViewById(R.id.btnLogin);
            Button btnSignup = findViewById(R.id.btnSignup);

            // Navigate to Login Activity
            btnLogin.setOnClickListener(v -> {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            });

            // Navigate to Signup Activity
            btnSignup.setOnClickListener(v -> {
                Intent signupIntent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            });
        }
    }

    private void navigateToMainPageActivity() {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        finish(); // Close MainActivity
    }
}
