package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.transition.ChangeBounds;
import android.widget.TextView;

public class About_Us extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us);
        getSupportActionBar().setTitle("");

        /*title = findViewById(R.id.about_us_title);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/acratica.otf");

        SpannableStringBuilder str = new SpannableStringBuilder("PETronus");

        str.setSpan(new CustomTypefaceSpan("",font),3,8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        title.setText(str);*/

    }
}