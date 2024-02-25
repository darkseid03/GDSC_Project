package com.example.legal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private FirebaseAuth firebaseAuth;

    private DatabaseReference userRef;
    private TextView usernameTextView;

    private void fetchAndSetUsername() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("name").getValue(String.class);
                    if (username != null) {
                        usernameTextView.setText(username);
                    } else {
                        Log.e(TAG, "User name is null");
                    }
                } else {
                    Log.e(TAG, "User data does not exist in the Realtime Database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching user data: " + databaseError.getMessage());
            }
        });
    }



    private ImageView profileImageView;


    private void fetchAndSetUserData() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("name").getValue(String.class);
                    String gender = dataSnapshot.child("Gender").getValue(String.class);
                    if (username != null) {
                        usernameTextView.setText(username);
                    } else {
                        Log.e(TAG, "User name is null");
                    }

                    
                    setProfilePicture(gender);
                } else {
                    Log.e(TAG, "User data does not exist in the Realtime Database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching user data: " + databaseError.getMessage());
            }
        });
    }

    private void setProfilePicture(String gender) {
        // Set different profile pictures based on gender
        if ("Male".equalsIgnoreCase(gender)) {
            profileImageView.setImageResource(R.drawable.avtar_png);
        } else if ("Female".equalsIgnoreCase(gender)) {
            profileImageView.setImageResource(R.drawable.img_2);
        } else if ("LGBTQAI+".equalsIgnoreCase(gender)) {
            profileImageView.setImageResource(R.drawable.img_3);
        } else {

            profileImageView.setImageResource(R.drawable.avtar_png);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#FF89E805"));
        }
        firebaseAuth = FirebaseAuth.getInstance();

        usernameTextView = findViewById(R.id.username);
        profileImageView = findViewById(R.id.profilePhoto);
        getSupportActionBar().hide();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            fetchAndSetUsername();
            fetchAndSetUserData();
        }



        ImageView editIcon = findViewById(R.id.editIcon);
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, ProfileSetupActivity.class);
                startActivity(intent);
            }
        });


        LinearLayout resetPasswordLayout = findViewById(R.id.resetPasswordLayout);
        resetPasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetPassword();
            }
        });


        LinearLayout changeEmailLayout = findViewById(R.id.changeEmailLayout);
        changeEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, HelpCentreActivity.class);
                startActivity(intent);
            }
        });


        LinearLayout logoutLayout = findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                Log.d(TAG, "User signed out");
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


        LinearLayout profileLayout = findViewById(R.id.profileLayout);
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "About activity started");
                Intent intent = new Intent(ProfileActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resetPassword() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Password reset email sent to: " + email);
                                Toast.makeText(ProfileActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "Failed to send reset email", task.getException());
                                Toast.makeText(ProfileActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Log.e(TAG, "No user signed in");
            Toast.makeText(this, "No user signed in", Toast.LENGTH_SHORT).show();
        }
    }


}
