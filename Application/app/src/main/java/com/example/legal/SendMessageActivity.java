package com.example.legal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SendMessageActivity extends AppCompatActivity {
    private EditText messageEditText, nameEditText;
    private TextView sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#21C9BE"));
        }
        messageEditText = findViewById(R.id.editTextMessage);
        nameEditText = findViewById(R.id.editTextName);
        sendButton = findViewById(R.id.buttonSend);


        ImageView backImageView = findViewById(R.id.imageView5);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().hide();
        ImageView backButton = findViewById(R.id.imageView5);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {

        String message = messageEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();


        if (!message.isEmpty() && !name.isEmpty()) {

            saveMessageToDatabase(name, message);
        } else {

            Toast.makeText(this, "Please enter both name and message", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveMessageToDatabase(String name, String message) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();


            String nodeName = name + "_" + uid + "_" + System.currentTimeMillis();


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


            DatabaseReference helpCenterReference = databaseReference.child("help center").child(nodeName);


            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("message", message);
            messageMap.put("uid", uid);


            helpCenterReference.setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        updateUIAfterMessageSent(nodeName);
                    } else {

                        Toast.makeText(SendMessageActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {

            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUIAfterMessageSent(String nodeName) {

        TextView sendButton = findViewById(R.id.buttonSend);
        sendButton.setVisibility(View.GONE);
        getSupportActionBar().hide();

        ImageView loadingGif = findViewById(R.id.loadingGif);
        loadingGif.setVisibility(View.VISIBLE);
        ImageView gif =findViewById(R.id.gif);
        gif.setVisibility(View.GONE);
        EditText name= findViewById(R.id.editTextName);
        EditText message =findViewById(R.id.editTextMessage);
        name.setVisibility(View.GONE);
        message.setVisibility(View.GONE);

        TextView uniqueKeyTextView = findViewById(R.id.uniqueKeyTextView);
        uniqueKeyTextView.setText("Message ID:  " + nodeName);
        uniqueKeyTextView.setVisibility(View.VISIBLE);


        TextView responseTextView = findViewById(R.id.responseTextView);
        responseTextView.setText("We will respond to you shortly.");
        responseTextView.setVisibility(View.VISIBLE);
    }


}
