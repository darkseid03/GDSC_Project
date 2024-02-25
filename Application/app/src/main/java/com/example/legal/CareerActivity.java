package com.example.legal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CareerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career);
        Button criminal=findViewById(R.id.btnCriminalLawyer);
        getSupportActionBar().hide();
        criminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CareerActivity.this, LawyerRegistrationActivity.class);
                intent.putExtra("lawyer_type","criminal");
                startActivity(intent);
            }
        });

        Button civil=findViewById(R.id.btnCivilLawyer);
        civil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CareerActivity.this, LawyerRegistrationActivity.class);
                intent.putExtra("lawyer_type","civil");
                startActivity(intent);
            }
        });
        Button others=findViewById(R.id.btnOthers);
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CareerActivity.this, LawyerRegistrationActivity.class);
                intent.putExtra("lawyer_type","others");
                startActivity(intent);
            }
        });


    }
}