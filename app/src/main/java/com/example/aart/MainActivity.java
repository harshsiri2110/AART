package com.example.aart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
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

    int imgCount;
    int postCount;

    SwipeRefreshLayout pullToRefresh;

    List<String> postList = new ArrayList<>();

    Model currModel = new Model();

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Listview for cards
        listView = (ListView) findViewById(R.id.listView);
        ImageView listPlaceholder = findViewById(R.id.listPlaceholder);
        Glide.with(this).load(R.drawable.loading_bar2).into(listPlaceholder);

        listView.setEmptyView(listPlaceholder);

        models = new ArrayList<>();
        if(!models.isEmpty())
        {
            models.clear();
        }

        showCards();

        //Swipe Refresh for cards
        pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        Log.d("TEST", "Hello I am here!");

        //On card click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this,"Position - "+i,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Details.class);
                intent.putExtra("selectedCard",(i+1));
                startActivity(intent);
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            getMenuInflater().inflate(R.menu.nav_menu, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.nav_menu_login, menu);
        }
        //getMenuInflater().inflate(R.menu.filter_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id)
        {
            case R.id.menu_sign_in:
            {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                break;
            }
            case R.id.menu_profile:
            {
                Toast.makeText(this, "Profile not made", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_register:
            {
                startActivity(new Intent(getApplicationContext(), Foster_reg.class));
                break;
            }
            case R.id.menu_logout:
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), FirstPage.class));
                finish();
                break;
            }
            case R.id.menu_upload:
            {
                startActivity(new Intent(getApplicationContext(), UploadForm.class));
                break;
            }
            case R.id.filter_icon:
            {

            }
        }

        return super.onOptionsItemSelected(item);
    }

}