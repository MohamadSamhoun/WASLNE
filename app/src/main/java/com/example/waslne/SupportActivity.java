package com.example.waslne;

import android.content.res.AssetManager;
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
import android.widget.Toast;

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SupportActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/";
    private Retrofit retrofit;
    private GeminiApiService geminiApiService;
    private EditText messageInput;
    private ImageButton sendButton;
    private LinearLayout chatLinearLayout;
    private ScrollView chatScrollView;
    private static final String TAG = "SupportActivity";
    private static final String FAQ_FILE = "faq.txt";
    private Map<String, String> faqMap = new HashMap<>();
    private static final String OUT_OF_SCOPE_RESPONSE = "I'm sorry, but that question is outside the scope of my current capabilities.";
    private static final double SIMILARITY_THRESHOLD = 0.8;

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

        // Creating Retrofit instance (requests)
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // Gson for JSON
                .build();

        geminiApiService = retrofit.create(GeminiApiService.class);
        BottomNavHelper.setupBottomNavigation(this, "support");
        initializeViews();
        loadFaq();
        setupSendButtonWithFaq();
    }

    private void loadFaq() {
        AssetManager assetManager = getAssets();
        try (InputStream inputStream = assetManager.open(FAQ_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String question;
            while ((question = reader.readLine()) != null) {
                String answer = reader.readLine();
                if (answer != null && !question.trim().isEmpty()) {
                    faqMap.put(question.trim().toLowerCase(), answer.trim());
                }
            }
            Log.d(TAG, "FAQ Loaded: " + faqMap.size() + " entries.");
        } catch (IOException e) {
            Log.e(TAG, "Error loading FAQ: " + e.getMessage());
            Toast.makeText(this, "Error loading support questions.", Toast.LENGTH_SHORT).show();
        }
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

    private void sendMessageToGemini(String userMessage) {
        String prompt = "You are a helpful and highly flexible support assistant. Your primary role is to answer the user's question using the provided information, even if the phrasing is different, reworded, or a partial match. Carefully consider the user's intent and try to find a relevant answer within the given material. Only respond with \"" + OUT_OF_SCOPE_RESPONSE + "\" if, after careful consideration, you can definitively say that the provided information offers absolutely no relevant context or answer to the user's query.\n\nProvided Information:\n";

        for (Map.Entry<String, String> entry : faqMap.entrySet()) {
            prompt += "Question: " + entry.getKey() + "\nAnswer: " + entry.getValue() + "\n\n";
        }

        prompt += "User Question: " + userMessage;

        //  GeminiRequest object containing prompt
        GeminiRequest request = new GeminiRequest(prompt);
        String apiKey = "AIzaSyAcF562TjGfpHPdtvGFkom_R1jdaJqwyhM";

        String requestUrl = BASE_URL + "v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
        Log.d(TAG, "Request URL: " + requestUrl);
        Log.d(TAG, "Request Body: " + new Gson().toJson(request));

        geminiApiService.sendMessageToGemini(request, apiKey).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    // Parse object
                    GeminiResponse geminiResponse = response.body();
                    // Extract 1st potential answer
                    if (geminiResponse.getCandidates() != null && geminiResponse.getCandidates().length > 0 &&
                            geminiResponse.getCandidates()[0].getContent() != null &&
                            geminiResponse.getCandidates()[0].getContent().getParts() != null &&
                            geminiResponse.getCandidates()[0].getContent().getParts().length > 0) {
                        String geminiMessage = geminiResponse.getCandidates()[0].getContent().getParts()[0].getText();
                        Log.d(TAG, "Gemini Response: " + geminiMessage);
                        addIncomingMessage(geminiMessage);
                    } else {
                        Log.w(TAG, "Received an empty or unexpected Gemini response.");
                        addIncomingMessage(OUT_OF_SCOPE_RESPONSE);
                    }
                } else {
                    String errorMessage = "Error: Unable to get a response from the AI.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += " Error Body: " + response.errorBody().string();
                            Log.e(TAG, "Error Response: " + errorMessage);
                        } catch (IOException e) {
                            Log.e(TAG, "Error reading error body: " + e.getMessage());
                        }
                    } else {
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

    private void setupSendButtonWithFaq() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = messageInput.getText().toString().trim().toLowerCase();
                if (!userMessage.isEmpty()) {
                    addOutgoingMessage(userMessage);
                    messageInput.setText("");
                    scrollToBottom();

                    String matchedAnswer = findMatchingAnswer(userMessage);
                    if (matchedAnswer != null) {
                        addIncomingMessage(matchedAnswer);
                    } else {
                        sendMessageToGemini(userMessage);
                    }
                }
            }
        });
    }

    private String findMatchingAnswer(String userQuestion) {
        // match lookup (faq)
        return faqMap.get(userQuestion);
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