package com.example.legal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class LawyerRegistrationActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextEmail, editTextBarCouncilNumber, editTextOfficeAddress;
    private TextView spinnerLawyerType;
    private Button btnRegisterAsLawyer;
    private Uri selectedImageUri;
    private EditText moneyEditText;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_registration);
        retrieveFCMToken();
        getSupportActionBar().hide();
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextBarCouncilNumber = findViewById(R.id.editTextBarCouncilNumber);
        editTextOfficeAddress = findViewById(R.id.editTextOfficeAddress);
        spinnerLawyerType = findViewById(R.id.spinnerLawyerType);
        btnRegisterAsLawyer = findViewById(R.id.btnRegisterAsLawyer);
        moneyEditText = findViewById(R.id.money);
        String lawyerType = getIntent().getStringExtra("lawyer_type");

       spinnerLawyerType.setText(lawyerType);
        Button btnChooseProfilePhoto = findViewById(R.id.btnChooseProfilePhoto);
        btnChooseProfilePhoto.setOnClickListener(v -> chooseProfilePhoto());
        btnRegisterAsLawyer.setOnClickListener(v -> registerAsLawyer());
    }

    private void registerAsLawyer() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String barCouncilNumber = editTextBarCouncilNumber.getText().toString().trim();
        String officeAddress = editTextOfficeAddress.getText().toString().trim();
        String lawyerType = getIntent().getStringExtra("lawyer_type");
        String money = moneyEditText.getText().toString();

        if (fullName.isEmpty() || email.isEmpty() || barCouncilNumber.isEmpty() || officeAddress.isEmpty() || money.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select a profile photo", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("LawyerRegistration", "Registering as lawyer with details: " +
                "FullName=" + fullName +
                ", Email=" + email +
                ", BarCouncilNumber=" + barCouncilNumber +
                ", OfficeAddress=" + officeAddress +
                ", LawyerType=" + lawyerType);

        uploadProfilePhoto();
    }

    private void uploadProfilePhoto() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        StorageReference photoRef = FirebaseStorage.getInstance().getReference()
                .child("profile_photos")
                .child(userId +System.currentTimeMillis() + "_profile_photo.jpg");

        photoRef.putFile(selectedImageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            saveUserDetails(uri.toString());
                            Log.d("LawyerRegistration", "Profile photo uploaded successfully. Photo URL: " + uri);

                        });
                    } else {
                        Toast.makeText(this, "Failed to upload profile photo", Toast.LENGTH_SHORT).show();
                        Log.e("LawyerRegistration", "Failed to upload profile photo. Error: " + task.getException());

                    }
                });
    }
    private String fcmToken;
    private void retrieveFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        fcmToken = task.getResult();
                        Log.d("LawyerRegistration", "FCM Token: " + fcmToken);
                    } else {
                        Log.e("LawyerRegistration", "Failed to get FCM token: " + task.getException());
                    }
                });
    }
    private void saveUserDetails(String photoUrl) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference careerUsersRef = FirebaseDatabase.getInstance().getReference().child("career_users");
        DatabaseReference userRef = careerUsersRef.child(editTextFullName.getText().toString().trim()+userId+System.currentTimeMillis());

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", editTextFullName.getText().toString().trim());
        userDetails.put("email", editTextEmail.getText().toString().trim());
        userDetails.put("barCouncilNumber", editTextBarCouncilNumber.getText().toString().trim());
        userDetails.put("officeAddress", editTextOfficeAddress.getText().toString().trim());
        userDetails.put("lawyerType",getIntent().getStringExtra("lawyer_type"));
        userDetails.put("photoUrl", photoUrl);
        userDetails.put("timestamp", System.currentTimeMillis());
        userDetails.put("money", moneyEditText.getText().toString().trim());
        userDetails.put("uid",mAuth.getCurrentUser().getUid());
        userDetails.put("fcmToken", fcmToken);

        userRef.setValue(userDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.e("SaveDetails", "Failed to save user details: " + task.getException());
                        Toast.makeText(this, "Failed to save user details", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void chooseProfilePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        Log.d("LawyerRegistration", "Choosing profile photo from gallery");

        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Log.d("LawyerRegistration", "Profile photo selected from gallery. Uri: " + selectedImageUri);

        }
    }
}
