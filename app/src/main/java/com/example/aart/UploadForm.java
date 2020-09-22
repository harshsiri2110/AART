package com.example.aart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UploadForm extends AppCompatActivity {
    EditText txtTitle,txtAge, location;
    Button btnupload, selectImages;
    DatabaseReference reff;

    RadioGroup radioGenderGroup;
    RadioButton txtGender;

    RecyclerView imgView;

    StorageReference storageReference;

    PostPreviewRecyclerAdapter postPreviewRecyclerAdapter;

    List<Uri> imageUriList = new ArrayList<Uri>();

    Member member;
    long maxId = 0;

    public Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_form);



        txtTitle = (EditText)findViewById(R.id.txttitle);
        txtAge = (EditText)findViewById(R.id.txtage);
        location = (EditText)findViewById(R.id.locatn);

        btnupload = (Button)findViewById(R.id.btnupload);
        selectImages = (Button) findViewById(R.id.selectImages);

        radioGenderGroup = (RadioGroup) findViewById(R.id.radioGroup);

        imgView = (RecyclerView) findViewById(R.id.imgView);

        imgView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));


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

        selectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooser();
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

                startActivity(new Intent(UploadForm.this,MainActivity.class));
            }
        }));
    }

    private void FileChooser()
    {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK )
        {
            if(data.getClipData() != null)
            {
                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i=0 ; i< totalItemsSelected;i++)
                {
                    imageUriList.add(data.getClipData().getItemAt(i).getUri());

                }

            }
            else if(data.getData() != null)
            {
                imageUriList.add(data.getData());
            }


            postPreviewRecyclerAdapter = new PostPreviewRecyclerAdapter(this,imageUriList);
            imgView.setAdapter(postPreviewRecyclerAdapter);
            postPreviewRecyclerAdapter.notifyDataSetChanged();

        }

    }
}