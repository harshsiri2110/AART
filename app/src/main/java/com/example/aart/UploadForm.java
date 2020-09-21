package com.example.aart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadForm extends AppCompatActivity {
EditText txttitle, txtage, locatn;
Button btnupload;
DatabaseReference reff;
Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_form);
        txttitle = (EditText)findViewById(R.id.txttitle);
        txtage = (EditText)findViewById(R.id.txtage);
        locatn = (EditText)findViewById(R.id.locatn);
        btnupload = (Button)findViewById(R.id.btnupload);
        member= new Member();
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        btnupload.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view){
                member.setTitle(txttitle.getText().toString());
                member.setAge(txtage.getText().toString().trim());
                member.setLocatn(locatn.getText().toString().trim());
                reff.push().setValue(member);
                Toast.makeText(UploadForm.this, "data inserted",Toast.LENGTH_LONG).show();
            }
        }));
    }
}