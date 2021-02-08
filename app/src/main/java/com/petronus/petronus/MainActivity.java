package com.petronus.petronus;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    //ViewPager viewPager;
    ListView listView;
    Adapter adapter;
    List<Model> models;

    ImageSlider mainActImageSlider;

    int foster_profile_pic = 0;
    float curr_age_in_months = 0;
    int imgCount;
    int postCount,dummy = 0;

    SwipeRefreshLayout pullToRefresh;

    FloatingActionButton addPostButton;

    List<String> postList = new ArrayList<>();
    boolean filter_on = false;

    TextView emptyListTextView;

    Model currModel = new Model();

    String filter_species,filter_gender,activity ="";

    int species = 0, gender = 0,filter_age;

    DatabaseReference reference, userRef;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

        if(!isConnected(this))
        {
            showCustomDialog();
        }

        //Listview for cards
        listView = (ListView) findViewById(R.id.listView);
        final ImageView listPlaceholder = findViewById(R.id.listPlaceholder);
        Glide.with(this).load(R.drawable.loading_bar2).into(listPlaceholder);


        listView.setEmptyView(listPlaceholder);



        emptyListTextView = findViewById(R.id.empty_list_text_view);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(listView.getAdapter().isEmpty())
                {
                    listPlaceholder.setImageResource(R.drawable.empty_list_2_removebg_preview);
                    emptyListTextView.setVisibility(View.VISIBLE);
                }
            }
        },6000);



        //Floating button
        addPostButton = (FloatingActionButton) findViewById(R.id.floatingActionButton3);

        models = new ArrayList<>();

        if(!models.isEmpty())
        {
            models.clear();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UploadForm.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        showCards();


        //Activity data
        Bundle bundle = getIntent().getExtras();

        activity = bundle.getString("Activity");

        if(activity.equals("Filter") && bundle.getString("Filter").equals("on"))
        {
            filter_species = bundle.getString("Species");
            filter_gender = bundle.getString("Gender");
            filter_age = bundle.getInt("Age");

            filter_on = true;

            filter_list();
        }

        if(activity.equals("FirstPage"))
        {
            filter_species = bundle.getString("FirstFilter");
            filter_on = true;
            if(filter_species.equals("Dog"))
            {
                species = 1;
            }
            else if(filter_species.equals("Cat"))
            {
                species = 2;
            }
        }

        //Swipe Refresh for cards
        pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);

        pullToRefresh.setDistanceToTriggerSync(500);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        Log.d("TEST", "Hello I am here!");

        //On card click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this,"Position - "+i,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Details.class);
                //intent.putExtra("selectedCard",postList.get(i));
                intent.putExtra("selectedCard",models.get(i).getID());

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private void filter_list()
    {
        switch (filter_species) {
            case "A Dog":
                species = 1;
                break;
            case "A Cat":
                species = 2;
                break;
            case "Both":
                species = 3;
                break;
        }

        switch (filter_gender) {
            case "Male":
                gender = 1;
                break;
            case "Female":
                gender = 2;
                break;
            case "Both":
                gender = 3;
                break;
        }
    }

    private void refreshList()
    {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void filter_all_attributes(int species,int gender, int age)
    {
        if (species == 1) {
            if (currModel.getSpeciesText().equals("Dog")) {
                filter_gender_and_age(gender, age);
            }
        }
        else if (species == 2) {
            if (currModel.getSpeciesText().equals("Cat")) {
                filter_gender_and_age(gender, age);
            }
        }
        else if (species == 3)
        {
            filter_gender_and_age(gender,age);
        }
    }

    private void filter_gender_and_age(int gender, int age)
    {
        if (gender == 1) {
            if (currModel.getGender().equals("Male")) {
                filter_cards_age(age);
            }
        }
        else if (gender == 2) {
            if (currModel.getGender().equals("Female")) {
                filter_cards_age(age);
            }
        }
        else if (gender == 3) {
            filter_cards_age(age);
        }
    }

    private void filter_species_and_age(int species, int age)
    {
        if (species == 1) {
            if (currModel.getSpeciesText().equals("Dog")) {
                filter_cards_age(age);
            }
        }
        else if (species == 2) {
            if (currModel.getSpeciesText().equals("Cat")) {
                filter_cards_age(age);
            }
        }
        else if (species == 3)
        {
            filter_cards_age(age);
        }
    }

    private void filter_species_and_gender(int species, int gender)
    {
        if (species == 1) {
            if (currModel.getSpeciesText().equals("Dog")) {
                Log.d("TEST","Calling filter_species_and_gender with species as Dog");
                filter_cards_gender(gender);
            }
        }
        else if (species == 2) {
            if (currModel.getSpeciesText().equals("Cat")) {
                Log.d("TEST","Calling filter_species_and_gender with species as Cat");
                    filter_cards_gender(gender);
            }
        }
        else if (species == 3)
        {
            filter_cards_gender(gender);
        }

    }

    private void filter_cards_age(int mAge)
    {
        switch (mAge)
        {
            case 0:
            {
                models.add(currModel);
                break;
            }
            case 1:
            {
                if(curr_age_in_months <= 1f) {
                    models.add(currModel);
                }
                break;
            }
            case 2:
            {
                if(curr_age_in_months <= 3f) {
                    models.add(currModel);
                }
                break;
            }
            case 3:
            {
                if(curr_age_in_months <= 6f) {
                    models.add(currModel);
                }
                break;
            }
            case 4:
            {
                if(curr_age_in_months <= 12f) {
                    models.add(currModel);
                }
                break;
            }
            case 5:
            {
                if(curr_age_in_months > 12f) {
                    models.add(currModel);
                }
                break;
            }
        }
    }

    private void filter_cards_species(int species)
    {
        if (species == 1) {
            if (currModel.getSpeciesText().equals("Dog")) {
                models.add(currModel);
            }
        }
        else if (species == 2) {
            if (currModel.getSpeciesText().equals("Cat")) {
                models.add(currModel);
            }
        }
        else if (species == 3) {
                models.add(currModel);
        }
    }

    private void filter_cards_gender(int gender)
    {
        if (gender == 1) {
            if (currModel.getGender().equals("Male")) {
                models.add(currModel);
            }
        }
        else if (gender == 2) {
            if (currModel.getGender().equals("Female")) {
                models.add(currModel);
            }
        }
        else if (gender == 3) {
                models.add(currModel);
        }
    }

    private void showCards()
    {
        reference = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if(!isConnected(MainActivity.this))
                {
                    showCustomDialog();
                }
                else {
                    postCount = Integer.MIN_VALUE;

                    for (final DataSnapshot currPostSnap : snapshot.getChildren()) {
                        currPostSnap.getRef().child("images").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot imgSnapshot) {

                                currModel = currPostSnap.getValue(Model.class);

                                imgCount = Integer.MIN_VALUE;
                                imgCount = (int) imgSnapshot.getChildrenCount();
                                //Log.d("TEST","Images count - "+imgCount);

                                for (DataSnapshot currImgSnap : imgSnapshot.getChildren()) {
                                    ImageUrl currImg = currImgSnap.getValue(ImageUrl.class);

                                    currModel.getImageList().add(currImg);

                                    adapter.notifyDataSetChanged();

                                    if (currModel.getImageList().size() == imgCount) {
                                        if (!postList.contains(currModel.getID())) {

                                            postList.add(currModel.getID());

                                            String[] ageText = currModel.getAge().split(" ");

                                            if (ageText[1].equals("Months")) {
                                                curr_age_in_months = Float.parseFloat(ageText[0]);
                                            } else if (ageText[1].equals("Year(s)")) {
                                                curr_age_in_months = Float.parseFloat(ageText[0]) * 12;
                                            }

                                            if (filter_on) {

                                                if (species > 0 && gender > 0 && filter_age > 0) {
                                                    filter_all_attributes(species, gender, filter_age);
                                                } else if (gender > 0 && filter_age > 0) {
                                                    filter_gender_and_age(gender, filter_age);
                                                } else if (species > 0 && filter_age > 0) {
                                                    filter_species_and_age(species, filter_age);
                                                } else if (species > 0 && gender > 0) {
                                                    Log.d("TEST", "Calling filter_species_and_gender");
                                                    filter_species_and_gender(species, gender);
                                                } else if (species > 0) {
                                                    filter_cards_species(species);
                                                } else if (gender > 0) {
                                                    filter_cards_gender(gender);
                                                } else if (filter_age >= 0) {
                                                    filter_cards_age(filter_age);
                                                }
                                            } else {
                                                models.add(currModel);
                                            }
                                            //Collections.sort(models, Collections.<Model>reverseOrder());
                                            Collections.sort(models, new Comparator<Model>() {
                                                @Override
                                                public int compare(Model o1, Model o2) {
                                                    return o2.getID().compareTo(o1.getID());
                                                }
                                            });
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

                    adapter = new Adapter(models, MainActivity.this, R.layout.temp_cards);

                    adapter.notifyDataSetChanged();

                    listView = findViewById(R.id.listView);
                    listView.setAdapter(adapter);

                    adapter.registerDataSetObserver(new DataSetObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            if (!listView.getAdapter().isEmpty()) {
                                addPostButton.setVisibility(View.VISIBLE);
                                if (firebaseAuth.getCurrentUser() == null) {
                                    addPostButton.setVisibility(View.INVISIBLE);
                                }
                                emptyListTextView.setEnabled(false);
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),FirstPage.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            getMenuInflater().inflate(R.menu.nav_menu, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.nav_menu_login, menu);
            FirebaseUser usr = firebaseAuth.getCurrentUser();
            if(usr != null) {
                final String uid = usr.getUid();
                userRef = FirebaseDatabase.getInstance().getReference().child("Foster").child("User");
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Fosterdetails fosterdetails = snapshot.child(uid).getValue(Fosterdetails.class);
                        menu.findItem(R.id.profile_icon).setIcon((int)fosterdetails.getProfilePic());
                        menu.findItem(R.id.profile_icon).setVisible(true);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
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
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            }
            case R.id.profile_icon:
            {
                startActivity(new Intent(getApplicationContext(), Foster_Profile.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                //Toast.makeText(this, "Profile nmade", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_register:
            {
                startActivity(new Intent(getApplicationContext(), Foster_reg.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            }
            case R.id.menu_logout:
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), FirstPage.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
                break;
            }
            case R.id.menu_upload:
            {
                startActivity(new Intent(getApplicationContext(), UploadForm.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            }
            case R.id.filter_icon:
            {
                startActivity(new Intent(getApplicationContext(), FilterPage.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            }

            case android.R.id.home:
            {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                        Intent intent = new Intent(getApplicationContext(), FirstPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isConnected(MainActivity firstPage) {
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

}