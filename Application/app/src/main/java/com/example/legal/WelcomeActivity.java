package com.example.legal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView imageView;

    private void setStatusBarGradient() {
        Window window = getWindow();
        View decorView = window.getDecorView();


        int startColor = Color.parseColor("#AFAAFE");
        int endColor = Color.parseColor("#5C4DFC");


        android.graphics.drawable.GradientDrawable gradientDrawable =
                new android.graphics.drawable.GradientDrawable(
                        android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{startColor, endColor}
                );


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            gradientDrawable.setGradientRadius(decorView.getHeight());
        }

        window.setStatusBarColor(Color.TRANSPARENT);
        window.setBackgroundDrawable(gradientDrawable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setStatusBarGradient();
        getSupportActionBar().hide();
        imageView = findViewById(R.id.imageView);




        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }


        TextView signInButton = findViewById(R.id.sign_in_button);
        TextView signUpButton = findViewById(R.id.sign_up_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
            }
        });
    }
}
