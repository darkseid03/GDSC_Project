package com.example.legal;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private CustomPagerAdapter pagerAdapter;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navView;
    private ImageView menuIcon,profile;

    private Handler handler;
    private Runnable updateRunnable;
    private FirebaseAuth mAuth;
    private ImageView careers,book1,chat1;
    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;
    private final long updateInterval = 2000;


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_carriers) {

            Intent careerIntent = new Intent(MainActivity.this, CareerActivity.class);
            startActivity(careerIntent);
        } else if (id == R.id.menu_book_appointment) {

            Intent appointmentIntent = new Intent(MainActivity.this, LawyersListActivity.class);
            startActivity(appointmentIntent);
        } else if (id == R.id.menu_get_consultation) {

            Intent consultationIntent = new Intent(MainActivity.this, ConsultancyActivity.class);
            startActivity(consultationIntent);
        } else if (id == R.id.menu_about) {

            Intent aboutIntent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.menu_settings) {

        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void animateImageViewFromBottom(ImageView imageView, int delay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 1500f, 0f);
        animator.setStartDelay(delay);
        animator.setDuration(1000);
        animator.start();
    }
    private ImageView buttonImageView;
    private ImageView bookImageView;
    private ImageView chatImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new CustomPagerAdapter(this);
      viewPager.setAdapter(pagerAdapter);
        getSupportActionBar().hide();

        buttonImageView = findViewById(R.id.button);
        bookImageView = findViewById(R.id.book);
        chatImageView = findViewById(R.id.chat);
        animateImageViewFromBottom(buttonImageView, 0);
        animateImageViewFromBottom(bookImageView, 200);
        animateImageViewFromBottom(chatImageView, 400);

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return MainActivity.this.onNavigationItemSelected(item);
            }
        });

        careers = findViewById(R.id.button);
        book1 =findViewById(R.id.book);
        chat1=findViewById(R.id.chat);

        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LawyersListActivity.class);
                startActivity(intent);
            }
        });


        careers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CareerActivity.class);
                startActivity(intent);
            }
        });


        chat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConsultancyActivity.class);
                startActivity(intent);
            }
        });



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //////////////////////
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            Log.d(TAG, "No user logged in, redirecting to LoginActivity");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            Log.d(TAG, "User logged in with UID: " + currentUser.getUid());
            checkUserProfile(currentUser.getUid());
        }

        ///////////////////////

        viewPager.setPageTransformer(true, new CustomPagerAdapter.DepthPageTransformer());

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu_icon);
profile=findViewById(R.id.profile_photo);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START, true);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });



        handler = new Handler(Looper.getMainLooper());
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int totalItems = pagerAdapter.getCount();
                if (currentItem < totalItems - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true);
                } else {
                    viewPager.setCurrentItem(0, true);
                }
                handler.postDelayed(this, updateInterval);
            }
        };
    }


    private void checkUserProfile(String userId) {
        DatabaseReference usersRef = mDatabase.child("Users");

        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.d(TAG, "Uid "+ userId);

                    Log.d(TAG, "User profile data does not exist, redirecting to ProfileSetupActivity");
                    startActivity(new Intent(MainActivity.this, ProfileSetupActivity.class));
                    finish();
                } else {

                    String gender = dataSnapshot.child("Gender").getValue(String.class);
                    setProfilePictureByGender(gender);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to check user profile: " + databaseError.getMessage());
                Toast.makeText(MainActivity.this, "Failed to check user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProfilePictureByGender(String gender) {
        ImageView profileImageView = findViewById(R.id.profile_photo);


        if ("Male".equalsIgnoreCase(gender)) {
            profileImageView.setImageResource(R.drawable.avtar_png);
        } else if ("Female".equalsIgnoreCase(gender)) {
            profileImageView.setImageResource(R.drawable.img_2);
        } else if ("LGBTQ+".equalsIgnoreCase(gender)) {
            profileImageView.setImageResource(R.drawable.img_3);
        } else {

            profileImageView.setImageResource(R.drawable.avtar_png);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(updateRunnable, updateInterval);
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacks(updateRunnable);
    }
}
