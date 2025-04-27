package com.example.waslne;

import android.content.res.Resources;
import android.os.Bundle;
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

public class SupportActivity extends AppCompatActivity {

    private EditText messageInput;
    private ImageButton sendButton;
    private LinearLayout chatLinearLayout;
    private ScrollView chatScrollView;

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

        // Initialize bottom navigation
        BottomNavHelper.setupBottomNavigation(this, "support");

        // Initialize UI elements
        initializeViews();

        // Set up send button click listener
        setupSendButton();
    }

    private void initializeViews() {
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatLinearLayout = findViewById(R.id.chatLinearLayout);
        chatScrollView = findViewById(R.id.chatScrollView);
    }

    private void setupSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    // Add the message to the chat
                    addOutgoingMessage(message);

                    // Clear the input field
                    messageInput.setText("");

                    // Scroll to the bottom
                    scrollToBottom();
                }
            }
        });
    }

    private void addOutgoingMessage(String message) {
        // Create a new LinearLayout with the exact properties from your XML
        LinearLayout messageContainer = new LinearLayout(this);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        messageContainer.setLayoutParams(containerParams);

        // Convert 8dp to pixels for padding
        int paddingInPx = dpToPx(8);
        messageContainer.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);
        messageContainer.setGravity(android.view.Gravity.END);

        // Create the TextView with the exact properties from your XML
        TextView messageText = new TextView(this);
        messageText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        messageText.setBackgroundResource(R.drawable.outgoing_message_background);

        // Convert 12dp to pixels for text padding
        int textPaddingInPx = dpToPx(12);
        messageText.setPadding(textPaddingInPx, textPaddingInPx, textPaddingInPx, textPaddingInPx);

        messageText.setText(message);
        messageText.setTextColor(getResources().getColor(android.R.color.white));
        messageText.setTextSize(16);

        // Add the TextView to the container
        messageContainer.addView(messageText);

        // Add the container to the chat layout
        chatLinearLayout.addView(messageContainer);
    }

    // Helper method to convert dp to pixels
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