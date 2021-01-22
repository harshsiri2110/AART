package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
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
        getSupportActionBar().setTitle("Login");

        if(!isConnected(this))
        {
            showCustomDialog();
        }

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

                if(!isConnected(LoginPage.this))
                {
                    showCustomDialog();
                }
                else {

                    //Loginbtn.setEnabled(false);
                    loadingDialog.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    }, 2000);

                    String memail = email.getText().toString().trim();
                    String mpassword = password.getText().toString().trim();

                    if (TextUtils.isEmpty(memail)) {
                        email.setError("Email is Required.");
                        return;
                    }

                    if (TextUtils.isEmpty(mpassword)) {
                        password.setError(("Password is Required."));
                        return;
                    }

                    if (mpassword.length() < 6) {
                        password.setError(("Password must be >= 6 characters"));
                        return;
                    }

                    firebaseAuth.signInWithEmailAndPassword(memail, mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginPage.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), FirstPage.class));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                Toast.makeText(LoginPage.this, "The username or password is incorrect or the user does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, Foster_reg.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
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

    private boolean isConnected(LoginPage firstPage) {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        }

        return super.onOptionsItemSelected(item);
    }
}