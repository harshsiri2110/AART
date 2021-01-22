package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.ChangeBounds;
import android.widget.ImageView;

public class Splash_Screen_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen_2);
        final ImageView logo = findViewById(R.id.splash_screen_app_logo);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Splash_Screen_2.this,logo,"first_page_logo");
                Intent intent = new Intent(Splash_Screen_2.this,FirstPage.class);
                startActivity(intent,options.toBundle());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);
            }
        },2000);
    }
}