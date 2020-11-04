package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Foster_Profile extends AppCompatActivity {

    TextView name, email;
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

        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        fosterPosts = findViewById(R.id.profile_listView);
        fosterPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this,"Position - "+i,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Foster_Profile.this,Details.class);
                intent.putExtra("selectedCard",postList.get(i));
                startActivity(intent);
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

        if(item.getItemId()== R.id.profile_edit){
            startActivity(new Intent(Foster_Profile.this, Edit_Profile.class).putExtra("name", fosterName).putExtra("email", fosterEmail).putExtra("mobileNumber", fosterNumber).putExtra("profilePic", profilePic));
            overridePendingTransition(R.anim.slide_in_bottom,R.anim.stationary_animation);
            //Toast.makeText(getApplicationContext(), "Edit post", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(Foster_Profile.this, MainActivity.class).putExtra("Activity","Foster_Profile"));
        //overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_bottom);
    }
}