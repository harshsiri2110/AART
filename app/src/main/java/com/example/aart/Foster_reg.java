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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Foster_reg extends AppCompatActivity
{

    EditText name,number,email,password,confirmPassword;

    Button submit;
    DatabaseReference reference;
    Fosterdetails fosterdetails;
    FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!= null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


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
                fosterdetails.setName(name.getText().toString());
                fosterdetails.setMobileNo(Long.parseLong(number.getText().toString()));
                fosterdetails.setEmail(email.getText().toString());
                fosterdetails.setPassword(password.getText().toString());
                currId = maxId + 1;

                String memail= email.getText().toString().trim();
                String mpassword = password.getText().toString().trim();

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
                firebaseAuth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Foster_reg.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            //(new Intent(Foster_reg.this,MainActivity.class));
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
    }
}