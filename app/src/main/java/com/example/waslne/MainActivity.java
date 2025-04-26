package com.example.waslne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    CardView signupCard;
    TextView signUpLink, closeSignup;

    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        signupCard = findViewById(R.id.signupCard);
        signUpLink = findViewById(R.id.textViewSignUp); // Your "Sign up?" link
        closeSignup = findViewById(R.id.closeSignup);   // The ✕ icon
        signIn = findViewById(R.id.signIn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupCard.setVisibility(View.VISIBLE);
                signupCard.setAlpha(0f);
                signupCard.setTranslationY(50f);

                signupCard.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(400)
                        .start();
            }
        });

        closeSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupCard.animate()
                        .alpha(0f)
                        .translationY(50f)
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                signupCard.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }
        });
    }

}
