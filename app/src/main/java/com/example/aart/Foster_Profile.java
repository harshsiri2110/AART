package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Foster_Profile extends AppCompatActivity {

    TextView name, email, empty_list_text;
    ImageView empty_list_image;
    TextView empty_list_button;
    ListView fosterPosts;
    AdapterFosterProfile adapter;

    String fosterName = "", fosterEmail = "";
    long fosterNumber = 0;
    Fosterdetails fosterdetails;

    String userId,postID;

    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    SwipeRefreshLayout pullToRefresh;

    CircleImageView circleImageView;

    List<Model> models ;
    List<String> postList = new ArrayList<>();
    int postCount, imgCount, profilePic;

    Model currModel = new Model();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster__profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        empty_list_button = findViewById(R.id.foster_profile_add_post_button);
        empty_list_image = findViewById(R.id.foster_profile_listPlaceholder);
        empty_list_text = findViewById(R.id.foster_profile_listPlaceholder_text);

        empty_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadForm.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        Handler handler = new Handler();



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fosterPosts.getAdapter().isEmpty()) {
                    empty_list_button.setVisibility(View.VISIBLE);
                    empty_list_button.setText("ADD A POST NOW!");
                    empty_list_image.setVisibility(View.VISIBLE);
                    empty_list_image.setImageResource(R.drawable.empty_list_2_removebg_preview);
                    empty_list_image.getLayoutParams().height = 600;
                    empty_list_text.setVisibility(View.VISIBLE);
                    empty_list_text.setText("You haven't uploaded anything yet!");
                }
            }
        },1000);

        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        fosterPosts = findViewById(R.id.profile_listView);
        fosterPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this,"Position - "+i,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Foster_Profile.this,Details.class);
                intent.putExtra("selectedCard",models.get(i).getID());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        circleImageView = findViewById(R.id.foster_profile_profile_photo);

        pullToRefresh = findViewById(R.id.foster_profile_swipeRefresh);

        pullToRefresh.setDistanceToTriggerSync(500);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Foster");

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();


        reference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fosterdetails = snapshot.child(userId).getValue(Fosterdetails.class);
                fosterName = fosterdetails.getName();
                fosterEmail = fosterdetails.getEmail();
                fosterNumber = fosterdetails.getMobileNo();
                profilePic = fosterdetails.getProfilePic();

                name.setText(fosterName);
                email.setText(fosterdetails.getEmail());
                circleImageView.setImageResource(fosterdetails.getProfilePic());

                showCards();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        models = new ArrayList<>();

        if(!models.isEmpty())
        {
            models.clear();
        }
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

                            assert currModel != null;
                            if (fosterEmail.equals(currModel.getfosterEmail()))
                            {
                                imgCount = Integer.MIN_VALUE;
                                imgCount = (int) imgSnapshot.getChildrenCount();
                                //Log.d("TEST","Images count - "+imgCount);

                                for (DataSnapshot currImgSnap : imgSnapshot.getChildren()) {
                                    ImageUrl currImg = currImgSnap.getValue(ImageUrl.class);

                                    currModel.getImageList().add(currImg);

                                    adapter.notifyDataSetChanged();

                                    if (currModel.getImageList().size() == imgCount) {
                                        if(!postList.contains(currModel.getID())) {
                                            postList.add(currModel.getID());
                                            models.add(currModel);
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                adapter = new AdapterFosterProfile(models, Foster_Profile.this,R.layout.foster_edit_post_card);

                adapter.notifyDataSetChanged();

                fosterPosts.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.edit_foster_profile, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {

            case R.id.profile_edit:
                startActivity(new Intent(Foster_Profile.this, Edit_Profile.class).putExtra("name", fosterName).putExtra("email", fosterEmail).putExtra("mobileNumber", fosterNumber).putExtra("profilePic", profilePic));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
                //Toast.makeText(getApplicationContext(), "Edit post", Toast.LENGTH_SHORT).show();

            case android.R.id.home:
            {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),FirstPage.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


}