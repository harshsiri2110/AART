package com.petronus.petronus;

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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    boolean emailUnique = true;

    int selectedImage = R.drawable.c1;

    long maxId = 0;
    long currId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster_reg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

        if(!isConnected(this))
        {
            showCustomDialog();
        }

        fosterdetails = new Fosterdetails();

        reference= FirebaseDatabase.getInstance().getReference().child("Foster").child("User");

        final LoadingDialog loadingDialog = new LoadingDialog(Foster_reg.this);

        name = findViewById(R.id.fosterName);
        number = findViewById(R.id.num);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        confirmPassword = findViewById(R.id.Cpass);

        submit = findViewById(R.id.btnreg);
        login = findViewById(R.id.mlogin);

        firebaseAuth = FirebaseAuth.getInstance();

        /*if(firebaseAuth.getCurrentUser()!= null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("Activity", "Foster_Reg"));
            finish();
        }*/

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

        submit.setOnClickListener((new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!isConnected(Foster_reg.this))
                {
                    showCustomDialog();
                }
                else {
                    if(name.getText().toString().equals("")
                            ||number.getText().toString().equals("")
                            ||email.getText().toString().equals("")
                            ||password.getText().toString().equals("")
                            ||confirmPassword.getText().toString().equals(""))
                    {
                        Toast.makeText(Foster_reg.this,
                                "Please fill in the required fields", Toast.LENGTH_LONG).show();
                    }
                    else {

                        if (checkNumberInput(name.getText().toString())) {
                            Toast.makeText(Foster_reg.this,
                                    "The field 'Full Name' cannot contain digits", Toast.LENGTH_LONG).show();
                        } else {
                            //submit.setEnabled(false);
                            loadingDialog.startLoadingDialog();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.dismissDialog();
                                }
                            }, 2000);

                            final String mname = name.getText().toString();
                            final long mnumber = Long.parseLong(number.getText().toString());

                            fosterdetails.setName(mname);
                            fosterdetails.setMobileNo(mnumber);
                            fosterdetails.setEmail(email.getText().toString());
                            fosterdetails.setPassword(password.getText().toString());
                            fosterdetails.setProfilePic(selectedImage);
                            currId = maxId + 1;

                            final String memail = email.getText().toString().trim();
                            final String mpassword = password.getText().toString().trim();
                            String mConfirmPass = confirmPassword.getText().toString().trim();

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

                            if (!mConfirmPass.equals(mpassword)) {
                                confirmPassword.setError("Password is not the same");
                                return;
                            }

                            firebaseAuth.createUserWithEmailAndPassword(memail, mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Foster_reg.this, "User created", Toast.LENGTH_SHORT).show();

                                        addUser(mname, memail, mnumber, selectedImage);
                                        startActivity(new Intent(getApplicationContext(), FirstPage.class));
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        //startActivity(new Intent(Foster_reg.this,MainActivity.class));
                                    } else {
                                        Toast.makeText(Foster_reg.this, "Email already exists. Please try another email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Foster_reg.this, LoginPage.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private boolean checkNumberInput(String str)
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

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Foster_reg.this);
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

    private boolean isConnected(Foster_reg firstPage) {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}