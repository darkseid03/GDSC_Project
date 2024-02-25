package com.example.legal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookAppointmentActivity extends AppCompatActivity {

    private static final String TAG = "BookAppointmentActivity";
    private static final String FCM_SERVER_KEY = "AAAAw0ZFgBY:APA91bFIW2ny0fhTMTHwiOkRK5ZsIDns-gIKp3g6oxfeVedVPIrL9WJZpZ1o14Rovd0zft-YQ_oTqt4rInuUgJLixzcPtiH3E7XtXOiRWoyVs9h91JfG7JCgUw2rhs4_clZ6SGfMY-F7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String lawyerName = intent.getStringExtra("lawyerName");
        String lawyerMoney = intent.getStringExtra("lawyerMoney");


        TextView lawyerNameTextView = findViewById(R.id.lawyerNameTextView);
        TextView lawyerMoneyTextView = findViewById(R.id.lawyerMoneyTextView);

        lawyerNameTextView.setText("Booking appointment with " + lawyerName);
        lawyerMoneyTextView.setText("Lawyer's Fee: " + lawyerMoney);


        sendFCMRequest(intent.getStringExtra("fcmtoken"), lawyerName);
    }


    private void sendFCMRequest(String lawyerUserId, String lawyerName) {
        new Thread(() -> {
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "key=" + FCM_SERVER_KEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Customize the FCM message content
                String fcmMessage = createFCMMessage(lawyerUserId, lawyerName);

                Log.d(TAG, "Sending FCM message to token: " + lawyerUserId);
                Log.d(TAG, "FCM message content: " + fcmMessage);

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(fcmMessage.getBytes());

                outputStream.flush();
                outputStream.close();

                int responseCode = conn.getResponseCode();
                String responseMessage = conn.getResponseMessage();

                Log.d(TAG, "FCM server response: " + responseCode + " - " + responseMessage);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "FCM message sent successfully");
                } else {
                    Log.e(TAG, "Failed to send FCM message. Response code: " + responseCode);
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error sending FCM message: " + e.getMessage(), e);
            }
        }).start();
    }



    private String createFCMMessage(String lawyerUserId, String lawyerName) {
        String title = "Appointment Request";
        String body = "You have a new appointment request from " + lawyerName;

        return "{\"to\":\"" + lawyerUserId + "\",\"notification\":{\"title\":\"" + title + "\",\"body\":\"" + body + "\"}}";
    }
}
