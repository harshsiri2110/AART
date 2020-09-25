package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Foster_reg extends AppCompatActivity
{

    EditText name,number,email,password,confirmPassword;

    Button submit;
    DatabaseReference reference;
    Fosterdetails fosterdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster_reg);

        reference= FirebaseDatabase.getInstance().getReference().child("Foster");

        name = findViewById(R.id.name);
        number = findViewById(R.id.num);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        confirmPassword = findViewById(R.id.Cpass);

        submit = findViewById(R.id.btnreg);

        submit.setOnClickListener((new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fosterdetails.setName(name.getText().toString());
                Long.parseLong(number.getText().toString());
                fosterdetails.setEmail(email.getText().toString());
                fosterdetails.setPassword(password.getText().toString());
            }
        }));
    }
}