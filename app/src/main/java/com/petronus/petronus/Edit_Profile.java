package com.petronus.petronus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_Profile extends AppCompatActivity implements View.OnClickListener {

    ImageButton c1,c2,c3,c4,d1,d2,d3,d4;
    CircleImageView profilePic;
    int profilePicture;

    EditText editName, editEmail, editNumber;
    Button btnEdit, btnDeleteProfile;

    DatabaseReference reference,postsRef;

    FirebaseAuth firebaseAuth;

    String name, email, mobileNumber;
    AlertDialog.Builder bob;

    View checkBoxView;

    String user_email;

    boolean delete_user_posts = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        if(!isConnected(this))
        {
            showCustomDialog();
        }

        final LoadingDialog loadingDialog = new LoadingDialog(Edit_Profile.this);

        bob = new AlertDialog.Builder(Edit_Profile.this);

        profilePic = findViewById(R.id.edit_profile_photo);
        firebaseAuth = FirebaseAuth.getInstance();

        c1 = (ImageButton) findViewById(R.id.cat_profile1);
        c2 = (ImageButton) findViewById(R.id.cat_profile2);
        c3 = (ImageButton) findViewById(R.id.cat_profile3);
        c4 = (ImageButton) findViewById(R.id.cat_profile4);
        d1 = (ImageButton) findViewById(R.id.dog_profile1);
        d2 = (ImageButton) findViewById(R.id.dog_profile2);
        d3 = (ImageButton) findViewById(R.id.dog_profile3);
        d4 = (ImageButton) findViewById(R.id.dog_profile4);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        d1.setOnClickListener(this);
        d2.setOnClickListener(this);
        d3.setOnClickListener(this);
        d4.setOnClickListener(this);

        editName = (EditText)findViewById(R.id.edit_profile_name);
        editEmail = (EditText)findViewById(R.id.edit_profile_email);
        editNumber = (EditText)findViewById(R.id.edit_profile_No);

        btnEdit = (Button)findViewById(R.id.edit_profile_btnapply);
        btnDeleteProfile = (Button)findViewById(R.id.edit_profile_btndelete);


        Bundle bundle = getIntent().getExtras();

        name= bundle.getString("name");
        email = bundle.getString("email");
        user_email = email;
        mobileNumber = String.valueOf(bundle.getLong("mobileNumber"));
        profilePicture = bundle.getInt("profilePic");

        profilePic.setImageResource(profilePicture);

        editName.setText(name);
        editEmail.setText(email);
        editNumber.setText(mobileNumber);

        reference = FirebaseDatabase.getInstance().getReference().child("Foster").child("User").child(firebaseAuth.getCurrentUser().getUid());

        postsRef = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts");

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(Edit_Profile.this))
                {
                    showCustomDialog();
                }
                else {
                    if (checkNumberInput(editName.getText().toString())) {
                        Toast.makeText(Edit_Profile.this,
                                "The field 'Full Name' cannot contain digits", Toast.LENGTH_LONG).show();
                    }
                    else {
                        btnEdit.setEnabled(false);


                        loadingDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                            }
                        }, 2000);

                        reference.child("name").setValue(editName.getText().toString());
                        reference.child("email").setValue(editEmail.getText().toString());
                        reference.child("mobileNo").setValue(Long.parseLong(editNumber.getText().toString()));
                        reference.child("profilePic").setValue(profilePicture);

                        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot post: snapshot.getChildren())
                                {
                                    final Model currModel = post.getValue(Model.class);
                                    if(user_email.equals(currModel.getfosterEmail()))
                                    {
                                        post.getRef().child("fosterName").setValue(editName.getText().toString());
                                        post.getRef().child("fosterNumber").setValue(Long.parseLong(editNumber.getText().toString()));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        startActivity(new Intent(Edit_Profile.this, FirstPage.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            }
        });



        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(Edit_Profile.this))
                {
                    showCustomDialog();
                }
                else {
                    btnDeleteProfile.setEnabled(false);
                    bob.setMessage("Are you sure you want to delete?");
                    bob.setTitle("Confirm Delete Profile");
                    bob.setView(checkBoxView);
                    bob.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            loadingDialog.startLoadingDialog();
                            reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {
                                    postsRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot post : snapshot.getChildren()) {
                                                final Model currPost = post.getValue(Model.class);
                                                if (user_email.equals(currPost.getfosterEmail())) {
                                                    post.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            for (ImageUrl img : currPost.getImageList()) {
                                                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(img.getUri());
                                                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(Edit_Profile.this, "Account and posts deleted!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                    continue;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    firebaseAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.dismiss();
                                            loadingDialog.dismissDialog();
                                            firebaseAuth.signOut();
                                            startActivity(new Intent(Edit_Profile.this, FirstPage.class));
                                        }
                                    });
                                }
                            });
                        }
                    });
                    bob.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            btnDeleteProfile.setEnabled(true);
                        }
                    });
                    AlertDialog alertDialog = bob.create();
                    alertDialog.show();
                }
            }
        });
    }
    private void showCustomDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Edit_Profile.this);
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
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean checkNumberInput( String str)
    {
        for(char ch : str.toCharArray())
        {
            if(ch >= '0' && ch <= '9')
            {
                return true;
            }
        }
        return false;
    }

    private boolean isConnected(Edit_Profile firstPage) {
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

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.cat_profile1:
                profilePic.setImageResource(R.drawable.c1);
                profilePicture = R.drawable.c1;
                break;
            case R.id.cat_profile2:
                profilePic.setImageResource(R.drawable.c2);
                profilePicture = R.drawable.c2;
                break;
            case R.id.cat_profile3:
                profilePic.setImageResource(R.drawable.c3);
                profilePicture = R.drawable.c3;
                break;
            case R.id.cat_profile4:
                profilePic.setImageResource(R.drawable.c4);
                profilePicture = R.drawable.c4;
                break;
            case R.id.dog_profile1:
                profilePic.setImageResource(R.drawable.d1);
                profilePicture = R.drawable.d1;
                break;
            case R.id.dog_profile2:
                profilePic.setImageResource(R.drawable.d2);
                profilePicture = R.drawable.d2;
                break;
            case R.id.dog_profile3:
                profilePic.setImageResource(R.drawable.d3);
                profilePicture = R.drawable.d3;
                break;
            case R.id.dog_profile4:
                profilePic.setImageResource(R.drawable.d4);
                profilePicture = R.drawable.d4;
                break;

            default:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
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