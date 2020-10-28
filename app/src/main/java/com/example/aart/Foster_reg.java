package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Foster_reg extends AppCompatActivity implements View.OnClickListener
{

    ImageButton c1,c2,c3,c4,d1,d2,d3,d4;
    CircleImageView profilePic;

    EditText name,number,email,password,confirmPassword;

    Button submit, login;
    DatabaseReference reference;
    Fosterdetails fosterdetails;
    FirebaseAuth firebaseAuth;

    int selectedImage = R.drawable.c1;

    long maxId = 0;
    long currId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster_reg);

        fosterdetails = new Fosterdetails();

        reference= FirebaseDatabase.getInstance().getReference().child("Foster");

        name = findViewById(R.id.fosterName);
        number = findViewById(R.id.num);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        confirmPassword = findViewById(R.id.Cpass);

        submit = findViewById(R.id.btnreg);
        login = findViewById(R.id.mlogin);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!= null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("Activity", "Foster_Reg"));
            finish();
        }

        profilePic = findViewById(R.id.foster_reg_profile_photo);

        c1 = (ImageButton) findViewById(R.id.cat_prof1);
        c2 = (ImageButton) findViewById(R.id.cat_prof2);
        c3 = (ImageButton) findViewById(R.id.cat_prof3);
        c4 = (ImageButton) findViewById(R.id.cat_prof4);
        d1 = (ImageButton) findViewById(R.id.dog_prof1);
        d2 = (ImageButton) findViewById(R.id.dog_prof2);
        d3 = (ImageButton) findViewById(R.id.dog_prof3);
        d4 = (ImageButton) findViewById(R.id.dog_prof4);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        d1.setOnClickListener(this);
        d2.setOnClickListener(this);
        d3.setOnClickListener(this);
        d4.setOnClickListener(this);


        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                maxId = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        submit.setOnClickListener((new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                final String mname = name.getText().toString();
                final long mnumber = Long.parseLong(number.getText().toString());

                fosterdetails.setName(mname);
                fosterdetails.setMobileNo(mnumber);
                fosterdetails.setEmail(email.getText().toString());
                fosterdetails.setPassword(password.getText().toString());
                fosterdetails.setProfilePic(selectedImage);
                currId = maxId + 1;

                final String memail= email.getText().toString().trim();
                String mpassword = password.getText().toString().trim();
                String mConfirmPass = confirmPassword.getText().toString().trim();

                if(TextUtils.isEmpty(memail)){
                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(mpassword)){
                    password.setError(("Password is Required."));
                    return;
                }

                if(mpassword.length() < 6){
                    password.setError(("Password must be >= 6 characters"));
                    return;
                }

                if(!mConfirmPass.equals(mpassword))
                {
                    confirmPassword.setError("Password is not the same");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful()){
                            Toast.makeText(Foster_reg.this, "User created", Toast.LENGTH_SHORT).show();
                            addUser(mname,memail,mnumber,selectedImage);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("Activity","Foster_REG"));
                            //startActivity(new Intent(Foster_reg.this,MainActivity.class));
                            }
                        else {
                            Toast.makeText(Foster_reg.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /*reference.child(String.valueOf(maxId+1)).setValue(fosterdetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void Void) {
                        Toast.makeText(Foster_reg.this,"Data inserted!",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Foster_reg.this,"Data NOT inserted!",Toast.LENGTH_LONG).show();
                    }
                });*/
                //startActivity(new Intent(Foster_reg.this,MainActivity.class));
            }
        }));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Foster_reg.this, LoginPage.class));
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.cat_prof1:
                selectedImage = R.drawable.c1;
                profilePic.setImageResource((int)selectedImage);
                break;
            case R.id.cat_prof2:
                selectedImage = R.drawable.c2;
                profilePic.setImageResource((int)selectedImage);
                break;
            case R.id.cat_prof3:
                selectedImage = R.drawable.c3;
                profilePic.setImageResource((int)selectedImage);
                break;
            case R.id.cat_prof4:
                selectedImage = R.drawable.c4;
                profilePic.setImageResource((int)selectedImage);
                break;
            case R.id.dog_prof1:
                selectedImage = R.drawable.d1;
                profilePic.setImageResource((int)selectedImage);
                break;
            case R.id.dog_prof2:
                selectedImage = R.drawable.d2;
                profilePic.setImageResource((int)selectedImage);
                break;
            case R.id.dog_prof3:
                selectedImage = R.drawable.d3;
                profilePic.setImageResource((int)selectedImage);
                break;
            case R.id.dog_prof4:
                selectedImage = R.drawable.d4;
                profilePic.setImageResource((int)selectedImage);
                break;

            default:
                break;
        }
    }

    private void addUser(String name, String email, long mobileNo, int selectedImg){
        Fosterdetails fosterdetails1 = new Fosterdetails(name, email, mobileNo, selectedImg);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        FirebaseDatabase.getInstance().getReference().child("Foster").child("User").child(uid).setValue(fosterdetails1);
    }
}