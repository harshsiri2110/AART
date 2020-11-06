package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    EditText email, password;
    Button Loginbtn, Createbtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final LoadingDialog loadingDialog = new LoadingDialog(LoginPage.this);

        email= findViewById(R.id.Lemail);
        password = findViewById(R.id.Lpass);
        firebaseAuth = FirebaseAuth.getInstance();
        Loginbtn = findViewById(R.id.btnlogin);
        Createbtn = findViewById(R.id.btncreate);
        firebaseAuth = FirebaseAuth.getInstance();


        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Loginbtn.setEnabled(false);
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                }, 2000);

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

                firebaseAuth.signInWithEmailAndPassword(memail, mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginPage.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("Activity","LoginPage"));

                        }
                        else{
                            Toast.makeText(LoginPage.this, "The username or password is incorrect or the user does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, Foster_reg.class));
            }
        });

    }

}