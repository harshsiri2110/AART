package com.petronus.petronus;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.transition.ChangeBounds;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstPage extends AppCompatActivity {
    Button regbtn, getLoginbtn;
    Button loginbtn;
    ImageButton dogSelect,catSelect;
    int backButtonCount = 0;

    ImageView logo,aboutUs,title;

    //RelativeLayout title;

    Handler handler;
    Runnable runnable;

    FirebaseAuth firebaseAuth;
    DatabaseReference userRef;

    TextView firstPageDescription2;

    //TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        regbtn = findViewById(R.id.btnreg);
        dogSelect = findViewById(R.id.dogSelect);
        catSelect = findViewById(R.id.first_page_CatSelect);
        getLoginbtn = findViewById(R.id.btnlogin);

        firebaseAuth = FirebaseAuth.getInstance();

        firstPageDescription2 = findViewById(R.id.first_page_description2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.first_page_toolbar,null);
        actionBar.setCustomView(v);
        getSupportActionBar().setTitle("");

        logo = v.findViewById(R.id.first_page_app_logo);
        //title = v.findViewById(R.id.first_page_toolbar_title_linear_layout);
        title = v.findViewById(R.id.first_page_title);
        /*Typeface font = Typeface.createFromAsset(getAssets(), "fonts/acratica.otf");

        SpannableStringBuilder str = new SpannableStringBuilder("PETronus");

        str.setSpan(new CustomTypefaceSpan("",font),3,8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        title.setText(str);*/
        aboutUs = findViewById(R.id.about_us_gif);

        //Glide.with(getApplicationContext()).clear(aboutUs);

        //Glide.with(FirstPage.this).load(R.drawable.about_us_gif_full).into(aboutUs);
        //aboutUs.setVisibility(View.INVISIBLE);

        if(!isConnected(this))
        {
            showCustomDialog();
        }

        rotateLogo();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new ChangeBounds().setDuration(500));
        }


        if(firebaseAuth.getCurrentUser() != null){
            regbtn.setVisibility(View.INVISIBLE);
            regbtn.setEnabled(false);
            getLoginbtn.setText("Sign in with another account");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getLoginbtn.setBackgroundTintList(ContextCompat.getColorStateList(this,
                        R.color.colorPrimaryDark));
            }
        }

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                //Glide.with(getApplicationContext()).clear(aboutUs);
                startActivity(new Intent(FirstPage.this,Foster_reg.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        dogSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                //Glide.with(getApplicationContext()).clear(aboutUs);
                startActivity(new Intent(FirstPage.this,MainActivity.class)
                        .putExtra("Activity","FirstPage")
                        .putExtra("FirstFilter","Dog"));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        catSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                //Glide.with(getApplicationContext()).clear(aboutUs);
                startActivity(new Intent(FirstPage.this,MainActivity.class)
                        .putExtra("Activity","FirstPage")
                        .putExtra("FirstFilter","Cat"));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        getLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                //Glide.with(getApplicationContext()).clear(aboutUs);
                startActivity(new Intent(FirstPage.this, LoginPage.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPage.this, About_Us.class);
                Pair pairs[] = new Pair[2];
                pairs[0] = new Pair<View, String> (logo, "first_page_logo");
                pairs[1] = new Pair<View, String> (title, "first_page_title");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FirstPage.this,pairs);

                startActivity(intent, options.toBundle());


                overridePendingTransition(R.anim.slide_in_bottom, R.anim.stationary_animation);
            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstPage.this);
        builder.setMessage("You're not connected to the internet! Please check your internet connection.")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitApp();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isConnected(FirstPage firstPage) {
        ConnectivityManager connectivityManager = (ConnectivityManager) firstPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isConnected(this))
        {
            showCustomDialog();
        }
    }


    void rotateLogo()
    {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotateAnimation = new RotateAnimation(0,360,
                        Animation.RELATIVE_TO_SELF,0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);

                rotateAnimation.setDuration(700);
                rotateAnimation.setRepeatCount(0);
                logo.startAnimation(rotateAnimation);

                /*//aboutUs.setVisibility(View.VISIBLE);
                //Glide.with(getApplicationContext()).asGif().load(R.drawable.about_us_gif_full).into(aboutUs);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //aboutUs.setVisibility(View.INVISIBLE);
                        //Glide.with(getApplicationContext()).clear(aboutUs);

                    }
                },4700);*/

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rotateLogo();
                    }
                },10000);

            }
        };
        handler.postDelayed(runnable,2000);
    }

    void exitApp()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                Intent.FLAG_ACTIVITY_CLEAR_TASK|
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finishAffinity();
    }

    @Override
    public void onBackPressed() {

        if(backButtonCount >= 1)
        {
            exitApp();
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application."
                    , Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null)
        {
            getMenuInflater().inflate(R.menu.first_page_profile, menu);
            FirebaseUser usr = firebaseAuth.getCurrentUser();
                final String uid = usr.getUid();
                userRef = FirebaseDatabase.getInstance().getReference().child("Foster").child("User");
                userRef.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Fosterdetails fosterdetails = snapshot.child(uid).getValue(Fosterdetails.class);
                        menu.findItem(R.id.first_page_profile_icon).setIcon((int)fosterdetails
                                .getProfilePic());
                        menu.findItem(R.id.first_page_logout).setVisible(true);
                        String[] name = fosterdetails.getName().split(" ");
                        firstPageDescription2.setText("HI "+name[0].toUpperCase()+"! NOT YOU?");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }
        //getMenuInflater().inflate(R.menu.filter_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.first_page_profile_icon:
                {
                    handler.removeCallbacks(runnable);
                    //Glide.with(getApplicationContext()).clear(aboutUs);
                startActivity(new Intent(getApplicationContext(), Foster_Profile.class));

                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
                }

                case R.id.first_page_logout:
                    {
                        handler.removeCallbacks(runnable);
                        //Glide.with(getApplicationContext()).clear(aboutUs);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), FirstPage.class));

                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    break;
                }

            case R.id.first_page_add_post:
            {
                handler.removeCallbacks(runnable);
                startActivity(new Intent(getApplicationContext(), UploadForm.class));

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}