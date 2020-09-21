package com.example.aart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UploadForm extends AppCompatActivity {
    EditText txtTitle,txtAge, location;
    Button btnupload;
    DatabaseReference reff;

    RadioGroup radioGenderGroup;
    RadioButton txtGender;

    Member member;
    long maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_form);

        txtTitle = (EditText)findViewById(R.id.txttitle);
        txtAge = (EditText)findViewById(R.id.txtage);
        location = (EditText)findViewById(R.id.locatn);

        btnupload = (Button)findViewById(R.id.btnupload);

        radioGenderGroup = (RadioGroup) findViewById(R.id.radioGroup);

        member= new Member();

        reff = FirebaseDatabase.getInstance().getReference().child("Member");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnupload.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view){

                int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                txtGender = (RadioButton) findViewById(selectedId);

                member.setTitle(txtTitle.getText().toString());
                member.setAge(txtAge.getText().toString());
                member.setLocation(location.getText().toString());
                member.setGender(txtGender.getText().toString());

                reff.child(String.valueOf(maxId+1)).setValue(member);


                Toast.makeText(UploadForm.this, "data inserted",Toast.LENGTH_LONG).show();
            }
        }));
    }
}