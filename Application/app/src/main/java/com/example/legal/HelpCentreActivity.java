package com.example.legal;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HelpCentreActivity extends AppCompatActivity {
    private void composeEmail(String emailAddress, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(HelpCentreActivity.this, "No email client found. Please visit our website for support.", Toast.LENGTH_SHORT).show();

 }
    }

    private void openInstagramProfile() {

        String instagramUsername = "legalaerie";
        Uri uri = Uri.parse("http://instagram.com/_u/" + instagramUsername);
        Intent instaIntent = new Intent(Intent.ACTION_VIEW, uri);
        instaIntent.setPackage("com.instagram.android");

        try {
            startActivity(instaIntent);
        } catch (ActivityNotFoundException e) {

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + instagramUsername)));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_centre);
        getSupportActionBar().hide();
        ImageView backButton = findViewById(R.id.imageView);
        LinearLayout buttonSendEmail = findViewById(R.id.changeEmailLayout);
        TextView buttonSendEmail1 = findViewById(R.id.changeEmailText);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#E6E5EA"));
        }

        ImageView backImageView = findViewById(R.id.imageView);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        LinearLayout instagramLayout = findViewById(R.id.instagramayout);
        instagramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagramProfile();
            }
        });

        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeEmail("Legalaerie@gmail.com", "Subject for Support");
            }
        });


        LinearLayout resetPasswordLayout = findViewById(R.id.resetPasswordLayout);
        resetPasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HelpCentreActivity.this, SendMessageActivity.class);
                startActivity(intent);
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}