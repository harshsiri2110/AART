package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    //ViewPager viewPager;
    ListView listView;
    Adapter adapter;
    List<Model> models;
    Button uploadPost, fosterRegBtn;

    DatabaseReference reference;
    List<Uri> uriList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reference = FirebaseDatabase.getInstance().getReference().child("Member");

        fosterRegBtn = findViewById(R.id.fosterRegBtn);
        //startActivity(new Intent(MainActivity.this,Cards.class));

        models = new ArrayList<>();
        models.add(new Model(R.drawable.dog, "Brown and white indie dog", "3 months", "Male", "Kothrud,  Pune","Very sweet dog"));
        /*
        models.add(new Model(R.drawable.dog, "Brown indie dog", "4 months", "Female", "Baner, Pune"));
        models.add(new Model(R.drawable.dog, "White indie dog", "1.5 months", "Male", "Warje, Pune"));
        models.add(new Model(R.drawable.dog, "Black indie dog", "2 months", "Female", "Aundh, Pune"));
        */

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                for(int i = 1 ; i <= snapshot.getChildrenCount(); i++)
                {
                    if(snapshot.hasChild(String.valueOf(i)))
                    {
                        DatabaseReference imgref= reference.child("images");
                        imgref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for(DataSnapshot currsnap : snapshot.getChildren())
                                {
                                    uriList.add(Uri.parse(currsnap.child("uri").getValue().toString()));
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                       models.add(new Model(uriList,
                               snapshot.child(String.valueOf(i)).child("title").getValue().toString(),
                               snapshot.child(String.valueOf(i)).child("age").getValue().toString(),
                               snapshot.child(String.valueOf(i)).child("gender").getValue().toString(),
                               snapshot.child(String.valueOf(i)).child("location").getValue().toString()));
                    }
                }

                adapter = new Adapter(models,MainActivity.this);
                listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
}
