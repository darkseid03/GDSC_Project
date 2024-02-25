package com.example.legal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.squareup.picasso.Picasso;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LawyersListActivity extends AppCompatActivity {

    private ListView lawyersListView;
    private DatabaseReference databaseReference;
    private ArrayList<Lawyer> lawyersList;
    private LawyerListAdapter lawyerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyers_list);
        getSupportActionBar().hide();
        lawyersListView = findViewById(R.id.lawyersListView);
        databaseReference = FirebaseDatabase.getInstance().getReference("career_users");
        lawyersList = new ArrayList<>();
        lawyerListAdapter = new LawyerListAdapter(this, lawyersList);
        lawyersListView.setAdapter(lawyerListAdapter);


        lawyersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lawyer selectedLawyer = lawyersList.get(position);
                bookAppointment(selectedLawyer);
            }
        });

        fetchLawyersList();
    }

    private void fetchLawyersList() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lawyersList.clear();
                for (DataSnapshot lawyerSnapshot : dataSnapshot.getChildren()) {
                    Lawyer lawyer = lawyerSnapshot.getValue(Lawyer.class);
                    if (lawyer != null) {
                        lawyersList.add(lawyer);
                    }
                }
                lawyerListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bookAppointment(Lawyer lawyer) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");
            String appointmentKey = appointmentsRef.push().getKey();
            if (appointmentKey != null) {
                appointmentsRef.child(appointmentKey).child("userUid").setValue(userId);
                appointmentsRef.child(appointmentKey).child("lawyerUid").setValue(lawyer.getUserId());
                appointmentsRef.child(appointmentKey).child("timestamp").setValue(System.currentTimeMillis());

            }

            Intent intent = new Intent(LawyersListActivity.this, BookAppointmentActivity.class);

            intent.putExtra("lawyerName", lawyer.getName());
            intent.putExtra("lawyerMoney", lawyer.getMoney());
            intent.putExtra("lawyeruserid", lawyer.getUserId());
            intent.putExtra("lawyerbarcouncil", lawyer.getBarCouncilNumber());
            intent.putExtra("lawyerphotourl", lawyer.getPhotoUrl());
            intent.putExtra("lawyeroffice", lawyer.getOfficeAddress());
            intent.putExtra("fcmtoken", lawyer.getFcmToken());


            startActivity(intent);
        }
    }
}
