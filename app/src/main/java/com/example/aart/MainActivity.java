package com.example.aart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity
{

    //ViewPager viewPager;
    ListView listView;
    Adapter adapter;
    List<Model> models;
    Button uploadPost, fosterRegBtn;

    int currPost;
    int backButtonCount = 0;

    DatabaseReference reference;
    List<String> uriList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] species = {"Dog","Cat"};

        reference = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts");

        fosterRegBtn = findViewById(R.id.fosterRegBtn);
        //startActivity(new Intent(MainActivity.this,Cards.class));

        models = new ArrayList<>();

        uriList = new ArrayList<>();
        //uriList.add("https://firebasestorage.googleapis.com/v0/b/aart-c7906.appspot.com/o/2%2Fimages98994436-5cf3-4f41-a3bc-9e98f469c114?alt=media&token=6c106bd9-d8b5-4ba0-93eb-db70b00bf5f2");
        //models.add(new Model(uriList, "Brown and white indie dog", "3 months", "Male", "Kothrud,  Pune"));
        /*
        models.add(new Model(R.drawable.dog, "Brown indie dog", "4 months", "Female", "Baner, Pune"));
        models.add(new Model(R.drawable.dog, "White indie dog", "1.5 months", "Male", "Warje, Pune"));
        models.add(new Model(R.drawable.dog, "Black indie dog", "2 months", "Female", "Aundh, Pune"));
        */

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for(int j = 0 ; j < 2 ; j++) {
                    //if (snapshot.hasChild(species[j])) {
                for(DataSnapshot speciesSnapShot : snapshot.getChildren())
                {
                        final DatabaseReference currReff = speciesSnapShot.getRef();

                        currReff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                //for (int i = 1; i <= snapshot.getChildrenCount(); i++) {
                                for(final DataSnapshot currPostSnap : snapshot.getChildren()){
                                    //currPost = i;
                                    uriList.clear();

                                    //if (snapshot.hasChild(String.valueOf(i))) {
                                        DatabaseReference imgref = currPostSnap.child("images").getRef();

                                        Log.d("URL", "I am here!");
                                        imgref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot imgSnapshot) {
                                                for (DataSnapshot currSnap : imgSnapshot.getChildren()) {
                                                    //Log.d("URL",currSnap.child("uri").getValue().toString());
                                                    uriList.add(currSnap.child("uri").getValue().toString());
                                                }

                                                models.add(new Model(uriList,
                                                        currPostSnap.child("title").getValue().toString(),
                                                        currPostSnap.child("age").getValue().toString(),
                                                        currPostSnap.child("gender").getValue().toString(),
                                                        currPostSnap.child("location").getValue().toString()));

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        for (String url : uriList) {
                                            Log.d("URL", url);
                                        }


                                    //}
                                }
                                adapter = new Adapter(models, MainActivity.this);
                                listView = findViewById(R.id.listView);
                                listView.setAdapter(adapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            Log.d("TEST", "Hello I am here!");

        uploadPost = findViewById(R.id.uploadPost);

        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,UploadForm.class));
            }
        });

        fosterRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Foster_reg.class));
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
}
