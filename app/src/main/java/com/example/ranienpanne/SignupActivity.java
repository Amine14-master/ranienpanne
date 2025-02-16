package com.example.ranienpanne;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private EditText etUsername, etPhoneNumber;
    private Spinner spinnerType;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        etUsername = findViewById(R.id.etSignupUsername);
        etPhoneNumber = findViewById(R.id.etSignupPhoneNumber);
        spinnerType = findViewById(R.id.spinnerSignupType);
        Button btnSignup = findViewById(R.id.btnSignup);

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Users");

        btnSignup.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String phone = etPhoneNumber.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();

            if (!username.isEmpty() && !phone.isEmpty()) {
                // Use phone and type as unique keys
                String userKey = phone + "_" + type;

                User user = new User(username, phone, type);

                database.child(userKey).setValue(user).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();

                        // Navigate to corresponding page based on user type
                        Intent intent;
                        switch (type) {
                            case "Mechanic":
                                intent = new Intent(SignupActivity.this, MechanicPage.class);
                                break;
                            case "Piece Detache":
                                intent = new Intent(SignupActivity.this, PieceDetachePage.class);
                                break;
                            case "Depannage":
                                intent = new Intent(SignupActivity.this, DepannagePage.class);
                                break;
                            default: // Simple user
                                intent = new Intent(SignupActivity.this, MainPageActivity.class);
                                break;
                        }

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
