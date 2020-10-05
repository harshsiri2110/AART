package com.example.aart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;
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
    Button uploadPost, logout;

    int currPost;
    int imgCount;
    int postCount;

    List<String> postList = new ArrayList<>();

    Model currModel = new Model();

    DatabaseReference reference;
    List<ImageUrl> uriList = new ArrayList<>();
    List<List<ImageUrl>>  uriListList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);


        models = new ArrayList<>();
        if(!models.isEmpty())
        {
            models.clear();
        }

        showCards();

        /*reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final DatabaseReference currPost = reference.child(snapshot.getKey());
                if(!uriList.isEmpty())
                {
                    uriList.clear();
                }

                currPost.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {

                        DatabaseReference imgReff = currPost.child("images");

                        imgReff.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot imgSnapshot) {
                                for (DataSnapshot currSnap : imgSnapshot.getChildren()) {
                                    //Log.d("URL",currSnap.child("uri").getValue().toString());
                                    uriList.add(currSnap.child("uri").getValue().toString());
                                }

                                Model currModel = new Model(uriList,
                                        snapshot.child("title").getValue().toString(),
                                        snapshot.child("age").getValue().toString(),
                                        snapshot.child("gender").getValue().toString(),
                                        snapshot.child("location").getValue().toString());


                                if(!models.contains(currModel)) {
                                    models.add(currModel);
                                }

                                uriList.clear();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                adapter = new Adapter(models,MainActivity.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

        Log.d("TEST", "Hello I am here!");

        uploadPost = findViewById(R.id.uploadPost);

        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,UploadForm.class));
            }
        });

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FirstPage.class));
            }
        });
    }

    private void showCards()
    {
        reference = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                postCount = Integer.MIN_VALUE;
                for(final DataSnapshot currPostSnap : snapshot.getChildren())
                {
                    currPostSnap.getRef().child("images").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot imgSnapshot) {

                            currModel = currPostSnap.getValue(Model.class);


                            imgCount = Integer.MIN_VALUE;
                            imgCount = (int)imgSnapshot.getChildrenCount();
                            Log.d("TEST","Images count - "+imgCount);

                            for(DataSnapshot currImgSnap : imgSnapshot.getChildren())
                            {
                                ImageUrl currImg = currImgSnap.getValue(ImageUrl.class);

                                currModel.getImageList().add(currImg);

                                adapter.notifyDataSetChanged();

                                if(currModel.getImageList().size() == imgCount)
                                {
                                    //uriListList.add(uriList);
                                    //Log.d("TEST","WAIT DONE!\n URI List Size - " + uriList.size());


                                    /*Model newModel = new Model(uriList,
                                            currModel.getTitle(),
                                            currModel.getAge(),
                                            currModel.getGender(),
                                            currModel.getLocation());*/

                                    if(!postList.contains(currModel.getID())) {

                                        postList.add(currModel.getID());

                                        models.add(currModel);

                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                adapter = new Adapter(models, MainActivity.this);
                listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}















/*// final String[] species = {"Dog","Cat"};
        final List<String> species = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts");

        fosterRegBtn = findViewById(R.id.fosterRegBtn);
        //startActivity(new Intent(MainActivity.this,Cards.class));

        models = new ArrayList<>();
        models.clear();



        uriList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(final DataSnapshot currPostSnap: snapshot.getChildren()){
                    uriList.clear();
                    DatabaseReference imgref= reference.child(currPostSnap.getKey()).child("images");

                    imgref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot imgSnapshot) {
                            for (DataSnapshot currSnap : imgSnapshot.getChildren()) {
                                //Log.d("URL",currSnap.child("uri").getValue().toString());
                                uriList.add(currSnap.child("uri").getValue().toString());
                            }

                            Model currModel = new Model(uriList,
                                    currPostSnap.child("title").getValue().toString(),
                                    currPostSnap.child("age").getValue().toString(),
                                    currPostSnap.child("gender").getValue().toString(),
                                    currPostSnap.child("location").getValue().toString());

                            if(!models.contains(currModel)) {
                                models.add(currModel);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                adapter = new Adapter(models, MainActivity.this);
                listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










//uriList.clear();
//uriList.add("https://firebasestorage.googleapis.com/v0/b/aart-c7906.appspot.com/o/2%2Fimages98994436-5cf3-4f41-a3bc-9e98f469c114?alt=media&token=6c106bd9-d8b5-4ba0-93eb-db70b00bf5f2");
//models.add(new Model(uriList, "Brown and white indie dog", "3 months", "Male", "Kothrud,  Pune"));
        /*
        models.add(new Model(R.drawable.dog, "Brown indie dog", "4 months", "Female", "Baner, Pune"));
        models.add(new Model(R.drawable.dog, "White indie dog", "1.5 months", "Male", "Warje, Pune"));
        models.add(new Model(R.drawable.dog, "Black indie dog", "2 months", "Female", "Aundh, Pune"));
        */









        /*final DatabaseReference speciesReff = reference.child(catorDog);
        speciesReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(final DataSnapshot currPostSnap: snapshot.getChildren()){
                    uriList.clear();
                    DatabaseReference imgref= currPostSnap.child("images").getRef();

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
                }
                adapter = new Adapter(models, MainActivity.this);
                listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


    /*reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //for(int j = 0 ; j < 2 ; j++) {
                //if (snapshot.hasChild(species[j])) {
            for(DataSnapshot speciesSnapShot : snapshot.getChildren())
            {
                    final DatabaseReference currReff = reference.child(speciesSnapShot.getKey());

                    currReff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot snapshot) {
                            //for (int i = 1; i <= snapshot.getChildrenCount(); i++) {
                            for(final DataSnapshot currPostSnap : snapshot.getChildren()){
                                //currPost = i;
                                uriList.clear();

                                //if (snapshot.hasChild(String.valueOf(i))) {
                                    DatabaseReference imgref = currReff.child(currPostSnap.getKey());

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
        });*/