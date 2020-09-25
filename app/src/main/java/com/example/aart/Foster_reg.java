package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Foster_reg extends AppCompatActivity {

    EditText name,number,email,password,confirmPassword;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster_reg);

        name = findViewById(R.id.name);
        number = findViewById(R.id.num);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        confirmPassword = findViewById(R.id.Cpass);

        submit = findViewById(R.id.btnreg);



    }
}