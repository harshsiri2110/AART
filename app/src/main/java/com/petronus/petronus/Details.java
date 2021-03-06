package com.petronus.petronus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.transition.ChangeBounds;
import android.view.MenuItem;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {

    DatabaseReference reference;

    String selectedCard = "";

    Model currModel = new Model();

    ImageSlider imageSlider;

    TextView title, age, gender, location, description, foster_name, foster_number, foster_location;

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    public void supportFinishAfterTransition() {
        super.supportFinishAfterTransition();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(Build.VERSION.SDK_INT > 21) {
            getWindow().setSharedElementEnterTransition(new android.transition.ChangeBounds().setDuration(300));
            getWindow().setSharedElementExitTransition(new ChangeBounds().setDuration(300));
        }

        if(!isConnected(this))
        {
            showCustomDialog();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        title = findViewById(R.id.details_title);
        age = findViewById(R.id.ageText);
        gender = findViewById(R.id.genderText);
        location = findViewById(R.id.locationText);
        //description = findViewById(R.id.about_me_text);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.details_view_pager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        imageSlider = findViewById(R.id.images);

        Bundle bundle = getIntent().getExtras();
        selectedCard = bundle.getString("selectedCard");

        getDetails(selectedCard);

    }
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Details.this);
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

    private boolean isConnected(Details firstPage) {
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

    class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void getDetails(String position)
    {
        reference = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts").child(position);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                currModel = snapshot.getValue(Model.class);

                snapshot.getRef().child("images").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot imgSnapshot) {
                        if(!isConnected(Details.this))
                        {
                            showCustomDialog();
                        }
                        int imgCount;
                        imgCount = (int)imgSnapshot.getChildrenCount();
                        //Log.d("TEST","Images count - "+imgCount);
                        List<SlideModel> slideModels = new ArrayList<>();

                        for(DataSnapshot currImgSnap : imgSnapshot.getChildren())
                        {
                            ImageUrl currImg = currImgSnap.getValue(ImageUrl.class);

                            currModel.getImageList().add(currImg);

                            if(currModel.getImageList().size() == imgCount)
                            {
                                title.setText(currModel.getTitle());
                                age.setText(currModel.getAge());
                                gender.setText(currModel.getGender());
                                location.setText(currModel.getLocation());
                                //description.setText(currModel.getDescription());


                                adapter.addFrag(new AboutMe(currModel.getDescription(),currModel.getFosterName(),String.valueOf(currModel.getFosterNumber())),"About Me");
                                adapter.addFrag(new MedicalDetails(currModel.getMedical()),"Medical History");

                                for (ImageUrl currUri : currModel.getImageList())
                                {
                                    //Log.d("TEST","Inside Adapter!, Uri - "+currUri.getUri());
                                    slideModels.add(new SlideModel(currUri.getUri(), ScaleTypes.CENTER_INSIDE));

                                    if(slideModels.size() == currModel.getImageList().size())
                                    {
                                        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

                                        viewPager.setAdapter(adapter);

                                        tabLayout.setupWithViewPager(viewPager);
                                    }
                                }
                            }
                        }
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        }

        return super.onOptionsItemSelected(item);
    }
}


