package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

    FirebaseAuth firebaseAuth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        regbtn = findViewById(R.id.btnreg);
        dogSelect = findViewById(R.id.dogSelect);
        catSelect = findViewById(R.id.first_page_CatSelect);
        getLoginbtn = findViewById(R.id.btnlogin);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            regbtn.setVisibility(View.INVISIBLE);
            regbtn.setEnabled(false);
            getLoginbtn.setText("Sign in with another account");
        }

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstPage.this,Foster_reg.class));
            }
        });

        dogSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstPage.this,MainActivity.class).putExtra("Activity","FirstPage").putExtra("FirstFilter","Dog"));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        catSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstPage.this,MainActivity.class).putExtra("Activity","FirstPage").putExtra("FirstFilter","Cat"));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        getLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstPage.this, LoginPage.class));
                overridePendingTransition(R.anim.slide_in_bottom,R.anim.stationary_animation);
            }
        });
    }


    @Override
    public void onBackPressed() {

        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
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
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Fosterdetails fosterdetails = snapshot.child(uid).getValue(Fosterdetails.class);
                        menu.findItem(R.id.first_page_profile_icon).setIcon((int)fosterdetails.getProfilePic());
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
            case R.id.first_page_profile_icon: {
                startActivity(new Intent(getApplicationContext(), Foster_Profile.class));
                overridePendingTransition(R.anim.slide_in_bottom,R.anim.stationary_animation);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}