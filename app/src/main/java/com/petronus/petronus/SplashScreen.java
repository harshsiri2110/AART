package com.petronus.petronus;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
        final Intent intent = new Intent(SplashScreen.this,FirstPage.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent,options.toBundle());
            }
        },2000);

    }
}
