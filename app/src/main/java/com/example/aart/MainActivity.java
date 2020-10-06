package com.example.aart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
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

    SwipeRefreshLayout pullToRefresh;

    List<String> postList = new ArrayList<>();

    Model currModel = new Model();

    DatabaseReference reference;
    List<ImageUrl> uriList = new ArrayList<>();
    List<List<ImageUrl>>  uriListList = new ArrayList<>();

    public void logoutreg(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), FirstPage.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        ImageView listPlaceholder = findViewById(R.id.listPlaceholder);
        Glide.with(this).load(R.drawable.loadingbar).into(listPlaceholder);

        listView.setEmptyView(listPlaceholder);

        models = new ArrayList<>();
        if(!models.isEmpty())
        {
            models.clear();
        }

        showCards();

        pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
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

        /*logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FirstPage.class));
            }
        });*/
    }

    private void refreshList()
    {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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
                    currPostSnap.getRef().child("images").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot imgSnapshot) {

                            currModel = currPostSnap.getValue(Model.class);

                            imgCount = Integer.MIN_VALUE;
                            imgCount = (int)imgSnapshot.getChildrenCount();
                            //Log.d("TEST","Images count - "+imgCount);

                            for(DataSnapshot currImgSnap : imgSnapshot.getChildren())
                            {
                                ImageUrl currImg = currImgSnap.getValue(ImageUrl.class);

                                currModel.getImageList().add(currImg);

                                adapter.notifyDataSetChanged();

                                if(currModel.getImageList().size() == imgCount)
                                {
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

                adapter.notifyDataSetChanged();

                listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),FirstPage.class));

    }
}