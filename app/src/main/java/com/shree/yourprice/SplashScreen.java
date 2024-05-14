package com.shree.yourprice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_TIMEOUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Adding animations to elements
        ImageView logoImageView = findViewById(R.id.imgLogo);
//        TextView titleTextView = findViewById(R.id.tvTitle);
        TextView sloganTextView = findViewById(R.id.tvSlogan);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        logoImageView.startAnimation(scaleAnimation);
//        titleTextView.startAnimation(fadeInAnimation);
        sloganTextView.startAnimation(fadeInAnimation);

        // Use a Handler to delay the transition to the MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Replace `MainActivity` with your main activity class.
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}