package com.example.legal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProfileSetupActivity extends AppCompatActivity {

    private EditText inputName, inputPhoneNumber, inputHostelNumber;
    private Spinner spinnerGender;
    private TextView btnSave;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        ImageView backButton = findViewById(R.id.buttonFilter2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#541A42"));
        }
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        inputName = findViewById(R.id.input_name);
        inputPhoneNumber = findViewById(R.id.input_phone_number);
        inputHostelNumber = findViewById(R.id.input_hostel_number);
        spinnerGender = findViewById(R.id.spinner_gender);
        btnSave = findViewById(R.id.savetext);




        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileInformation();
            }
        });
    }

    private void saveProfileInformation() {
        String name = inputName.getText().toString().trim();
        String phoneNumber = inputPhoneNumber.getText().toString().trim();
        String hostelNumber = inputHostelNumber.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(hostelNumber) || TextUtils.isEmpty(gender)) {
            Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        DatabaseReference currentUserRef = databaseReference.child(userId);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userUID = currentUser.getUid();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();

                        currentUserRef.child("name").setValue(name);
                        currentUserRef.child("phoneNumber").setValue(phoneNumber);
                        currentUserRef.child("Address").setValue(hostelNumber);
                        currentUserRef.child("Gender").setValue(gender);
                        currentUserRef.child("fcmToken").setValue(token);

                        currentUserRef.child("uid").setValue(userUID);

                        Toast.makeText(getApplicationContext(), "Profile information saved successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ProfileSetupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
