package com.example.ranienpanne;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class AskAIActivity extends AppCompatActivity {

    private EditText userInputEditText;
    private Button askAIButton;
    private TextView responseTextView;

    private OpenAIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_ai);

        userInputEditText = findViewById(R.id.editTextUserInput);
        askAIButton = findViewById(R.id.buttonAskAI);
        responseTextView = findViewById(R.id.textViewResponse);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/") // Base URL for OpenAI
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(OpenAIService.class);

        askAIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = userInputEditText.getText().toString();
                if (!userInput.isEmpty()) {
                    askAI(userInput);
                } else {
                    Toast.makeText(AskAIActivity.this, "Please enter a question!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void askAI(String userMessage) {
        // Create the request payload
        List<ChatRequest.Message> messages = new ArrayList<>();
        messages.add(new ChatRequest.Message("user", userMessage));
        ChatRequest request = new ChatRequest("gpt-3.5-turbo", messages);

        // Make the API call
        apiService.getChatResponse(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String reply = response.body().choices.get(0).message.content;
                    responseTextView.setText(reply); // Display the response
                } else {
                    responseTextView.setText("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                responseTextView.setText("Failed: " + t.getMessage());
            }
        });
    }

    // Retrofit Service Interface
    interface OpenAIService {
        @Headers("Authorization: your api") // Replace with your API key
        @POST("v1/chat/completions")
        Call<ChatResponse> getChatResponse(@Body ChatRequest request);
    }

    // Request Model
    static class ChatRequest {
        private String model;
        private List<Message> messages;

        public ChatRequest(String model, List<Message> messages) {
            this.model = model;
            this.messages = messages;
        }

        static class Message {
            private String role;
            private String content;

            public Message(String role, String content) {
                this.role = role;
                this.content = content;
            }
        }
    }

    // Response Model
    static class ChatResponse {
        @SerializedName("choices")
        private List<Choice> choices;

        static class Choice {
            @SerializedName("message")
            private Message message;

            static class Message {
                @SerializedName("role")
                private String role;

                @SerializedName("content")
                private String content;
            }
        }
    }
}
