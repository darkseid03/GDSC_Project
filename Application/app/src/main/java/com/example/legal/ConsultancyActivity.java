
package com.example.legal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class ConsultancyActivity extends AppCompatActivity {

    private static final String TAG = "ConsultancyActivity";
    private EditText problemEditText;
    private Button submitButton;
    private RecyclerView answersRecyclerView;
    private AnswerAdapter answerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultancy);
        getSupportActionBar().hide();
        problemEditText = findViewById(R.id.problemEditText);
        submitButton = findViewById(R.id.submitButton);
        answersRecyclerView = findViewById(R.id.answersRecyclerView);


        answerAdapter = new AnswerAdapter();
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        answersRecyclerView.setAdapter(answerAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userProblem = problemEditText.getText().toString().trim();
                if (!userProblem.isEmpty()) {

                    sendToChatGPT(userProblem);
                }
            }
        });
    }

    private void sendToChatGPT(String userProblem) {

        ChatGPTHandler.sendMessage(userProblem, new ChatGPTHandler.ChatGPTResponseListener() {
            @Override
            public void onChatGPTResponse(String response) {

                new Handler(Looper.getMainLooper()).post(() -> addAnswer(response));
            }

            @Override
            public void onError(String error) {

                Log.e(TAG, "ChatGPT Error: " + error);
            }
        });
    }

    private void addAnswer(String answer) {

        answerAdapter.addAnswer(answer);

        problemEditText.setText("");
    }
}
