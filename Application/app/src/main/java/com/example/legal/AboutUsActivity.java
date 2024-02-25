package com.example.legal;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
        getSupportActionBar().hide();
        TextView aboutTextView = findViewById(R.id.aboutText);


        String descriptionText = "Welcome to LEGALAERIE, where integrity, excellence, and justice are our guiding principles. " +
                "With a steadfast commitment to upholding the highest ethical standards, we tirelessly advocate for our clients, " +
                "ensuring their rights are protected and their voices heard. Our team of dedicated attorneys brings a wealth " +
                "of expertise and experience to every case, delivering personalized solutions and achieving favorable outcomes. " +
                "Whether navigating complex legal matters or providing compassionate guidance through challenging times, " +
                "we are here to serve as your trusted legal partner. Experience the difference with LEGALAERIE, where providing " +
                "you justice is our priority.";

        aboutTextView.setText(descriptionText);
    }
}
