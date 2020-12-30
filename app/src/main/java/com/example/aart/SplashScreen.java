package com.example.aart;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.splash_screen_app_logo);

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        final ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,logo,"first_page_logo");

        Thread thread = new Thread()
        {
            @Override
            public void run() {

                try {
                    sleep(2000);
                    Intent intent = new Intent(SplashScreen.this,FirstPage.class);
                    startActivity(intent,options.toBundle());
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();
    }
}
