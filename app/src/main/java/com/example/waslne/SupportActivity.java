package com.example.waslne;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.gson.Gson;
import java.io.IOException;

public class SupportActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/";
    private Retrofit retrofit;
    private GeminiApiService geminiApiService;
    private EditText messageInput;
    private ImageButton sendButton;
    private LinearLayout chatLinearLayout;
    private ScrollView chatScrollView;
    private static final String TAG = "SupportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_support);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geminiApiService = retrofit.create(GeminiApiService.class);
        BottomNavHelper.setupBottomNavigation(this, "support");
        initializeViews();
        setupSendButton();
    }

    private void initializeViews() {
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatLinearLayout = findViewById(R.id.chatLinearLayout);
        chatScrollView = findViewById(R.id.chatScrollView);
    }

    private void addIncomingMessage(String message) {
        LinearLayout messageContainer = new LinearLayout(this);
        messageContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        messageContainer.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        messageContainer.setGravity(android.view.Gravity.START);
        TextView messageText = new TextView(this);
        messageText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        messageText.setBackgroundResource(R.drawable.incoming_message_background);
        messageText.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
        messageText.setText(message);
        messageText.setTextColor(getResources().getColor(android.R.color.black));
        messageText.setTextSize(16);
        messageContainer.addView(messageText);
        chatLinearLayout.addView(messageContainer);
        scrollToBottom();
    }

    private void sendMessageToGemini(String message) {
        GeminiRequest request = new GeminiRequest(message);
        String apiKey = "AIzaSyAcF562TjGfpHPdtvGFkom_R1jdaJqwyhM";


        String requestUrl = BASE_URL + "v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
        Log.d(TAG, "Request URL: " + requestUrl);
        Log.d(TAG, "Request Body: " + new Gson().toJson(request));

        geminiApiService.sendMessageToGemini(request, apiKey).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    GeminiResponse geminiResponse = response.body();
                    if (geminiResponse.getCandidates() != null && geminiResponse.getCandidates().length > 0 &&
                            geminiResponse.getCandidates()[0].getContent() != null &&
                            geminiResponse.getCandidates()[0].getContent().getParts() != null &&
                            geminiResponse.getCandidates()[0].getContent().getParts().length > 0) {
                        String geminiMessage = geminiResponse.getCandidates()[0].getContent().getParts()[0].getText();
                        Log.d(TAG, "Response: " + geminiMessage);
                        addIncomingMessage(geminiMessage);
                    } else {
                        Log.w(TAG, "Received an empty or unexpected response structure.");
                        addIncomingMessage("The AI didn't provide a response.");
                    }
                } else {
                    String errorMessage = "Error: Unable to get a response.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += " Error Body: " + response.errorBody().string();
                            Log.e(TAG, "Error Response: " + errorMessage);
                        } catch (IOException e) {
                            Log.e(TAG, "Error reading error body: " + e.getMessage());
                        }
                    }
                    else{
                        Log.e(TAG, "Error Response: " + response.message());
                    }
                    addIncomingMessage(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                addIncomingMessage("Error: " + t.getMessage());
            }
        });
    }

    private void setupSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    addOutgoingMessage(message);
                    messageInput.setText("");
                    scrollToBottom();
                    sendMessageToGemini(message);
                }
            }
        });
    }

    private void addOutgoingMessage(String message) {
        LinearLayout messageContainer = new LinearLayout(this);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        messageContainer.setLayoutParams(containerParams);
        int paddingInPx = dpToPx(8);
        messageContainer.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);
        messageContainer.setGravity(android.view.Gravity.END);
        TextView messageText = new TextView(this);
        messageText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        messageText.setBackgroundResource(R.drawable.outgoing_message_background);
        int textPaddingInPx = dpToPx(12);
        messageText.setPadding(textPaddingInPx, textPaddingInPx, textPaddingInPx, textPaddingInPx);
        messageText.setText(message);
        messageText.setTextColor(getResources().getColor(android.R.color.white));
        messageText.setTextSize(16);
        messageContainer.addView(messageText);
        chatLinearLayout.addView(messageContainer);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private void scrollToBottom() {
        chatScrollView.post(new Runnable() {
            @Override
            public void run() {
                chatScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}

