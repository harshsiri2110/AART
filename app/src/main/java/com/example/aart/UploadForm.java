package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadForm extends AppCompatActivity {
    EditText txttitle, locatn;
    Button btnupload;
    DatabaseReference reff;

    String gender;

    RadioGroup radioGenderGroup;
    RadioButton selectedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_form);
        txttitle=(EditText)findViewById(R.id.txttitle);
        locatn= (EditText)findViewById(R.id.locatn);

        radioGenderGroup = (RadioGroup) findViewById(R.id.radioGroup);

        btnupload= (Button)findViewById(R.id.btnupload);

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGenderGroup.getCheckedRadioButtonId();

                selectedButton = (RadioButton)findViewById(selectedId);

                gender = selectedButton.getText().toString();

            }
        });

        reff= FirebaseDatabase.getInstance().getReference().child("Member");

    }
}