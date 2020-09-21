package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadForm extends AppCompatActivity {
EditText txttitle, txtage, locatn;
Button btnupload;
DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_form);
        txttitle=(EditText)findViewById(R.id.txttitle);
        txtage=(EditText)findViewById(R.id.txtage);
        locatn= (EditText)findViewById(R.id.locatn);
        btnupload= (Button)findViewById(R.id.btnupload);
        reff= FirebaseDatabase.getInstance().getReference().child("Member");
    }
}