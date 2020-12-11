package com.example.aart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_Profile extends AppCompatActivity implements View.OnClickListener {

    ImageButton c1,c2,c3,c4,d1,d2,d3,d4;
    CircleImageView profilePic;
    int profilePicture;

    EditText editName, editEmail, editNumber;
    Button btnEdit, btnDeleteProfile;

    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    String name, email, mobileNumber;
    AlertDialog.Builder bob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        mobileNumber = String.valueOf(bundle.getLong("mobileNumber"));
        profilePicture = bundle.getInt("profilePic");

        profilePic.setImageResource(profilePicture);

        editName.setText(name);
        editEmail.setText(email);
        editNumber.setText(mobileNumber);

        reference = FirebaseDatabase.getInstance().getReference().child("Foster").child("User").child(firebaseAuth.getCurrentUser().getUid());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                startActivity(new Intent(Edit_Profile.this, MainActivity.class).putExtra("Activity","Edit_profile"));

            }
        });

        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDeleteProfile.setEnabled(false);
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                }, 2000);

                bob.setMessage("Are you sure you want to delete?");
                bob.setTitle("Confirm Delete Profile");
                bob.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {

                                firebaseAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog.dismiss();
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

                    }
                });
                AlertDialog alertDialog = bob.create();
                alertDialog.show();
            }
        });
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
}