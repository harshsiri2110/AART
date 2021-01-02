package com.example.aart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes;

public class UploadForm extends AppCompatActivity
{
    EditText txtTitle, txtAge, location, description, medical;
    Button btnupload, selectImages;
    DatabaseReference reff, reff2;
    String fosterEmail, fosterName, timeStamp, age_dropdown;

    RadioGroup radioGenderGroup, radioSpeciesGroup;
    RadioButton txtGender, txtSpecies;
    ImageSlider imgView;

    Spinner spinner;

    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    List<Uri> imageUriList = new ArrayList<Uri>();

    List<SlideModel> previewImages = new ArrayList<SlideModel>();

    List<ImageUrl> imageUrls = new ArrayList<>();

    Model model;

    long maxId = 0;
    long currId;


    String catOrDog;
    Map<String,String> uploadUrl = new HashMap<>();

    List<String> fileUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Post");

        final LoadingDialog loadingDialog = new LoadingDialog(UploadForm.this);
        storageReference = FirebaseStorage.getInstance().getReference();

        description = (EditText)findViewById(R.id.postDescription);
        medical= (EditText)findViewById(R.id.medicalDetails);
        txtTitle = (EditText)findViewById(R.id.txttitle);
        txtAge = (EditText)findViewById(R.id.txtage);
        location = (EditText)findViewById(R.id.locatn);

        btnupload = (Button)findViewById(R.id.btnupload);
        selectImages = (Button) findViewById(R.id.selectImages);

        radioGenderGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioSpeciesGroup = (RadioGroup) findViewById(R.id.speciesSelect);

        imgView = (ImageSlider) findViewById(R.id.imgView);

        spinner =(Spinner) findViewById(R.id.dropdown_age);

        firebaseAuth = FirebaseAuth.getInstance();

        model = new Model();

        timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        reff = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts");
        reff2 = FirebaseDatabase.getInstance().getReference().child("Foster").child("User");

        /*final AlertDialog.Builder confirmDel = new AlertDialog.Builder(this);

        imgView.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(final int i) {
                confirmDel.setMessage("Are you sure you want to delete this picture?");
                confirmDel.setTitle("Confirm Delete Picture");
                confirmDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageUriList.remove(imageUriList.get(i));
                        previewImages.remove(previewImages.get(i));
                        imgView.setImageList(previewImages,ScaleTypes.FIT);
                    }
                });
                confirmDel.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = confirmDel.create();
                alertDialog.show();
            }
        });
*/
        ArrayAdapter <String> adapter = new ArrayAdapter <String> (UploadForm.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Age));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        reff2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String currUser = firebaseAuth.getCurrentUser().getUid();

                Fosterdetails currFoster = snapshot.child(currUser).getValue(Fosterdetails.class);

                fosterEmail = currFoster.getEmail();
                fosterName = currFoster.getName();

                //model.setFosterName(fosterName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reff.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                maxId = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        selectImages.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FileChooser();
            }
        });


        btnupload.setOnClickListener((new View.OnClickListener()
        {
            @Override
            public void onClick(View view){

                btnupload.setEnabled(false);
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                }, 2000);

                int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                txtGender = (RadioButton) findViewById(selectedId);

                selectedId= radioSpeciesGroup.getCheckedRadioButtonId();
                txtSpecies= (RadioButton)findViewById(selectedId);

                model.setTitle(txtTitle.getText().toString());
                model.setAge(txtAge.getText().toString() + " " + spinner.getSelectedItem().toString());
                model.setLocation(location.getText().toString());
                model.setGender(txtGender.getText().toString());
                model.setDescription(description.getText().toString());
                model.setMedical(medical.getText().toString());
                model.setSpeciesText(txtSpecies.getText().toString());
                model.setID(timeStamp);
                //model.setID(UUID.randomUUID().toString());
                model.setfosterEmail(fosterEmail);
                model.setFosterName(fosterName);

                reff.child(timeStamp).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        startActivity(new Intent(UploadForm.this,MainActivity.class).putExtra("Activity","UploadForm"));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    }
                });

                if(!imageUriList.isEmpty())
                {
                    for(int i = 0 ; i < imageUriList.size();i++)
                    {
                        uploadPicture(imageUriList.get(i));
                    }
                }



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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK )
        {
            if(data.getClipData() != null)
            {
                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++)
                {
                    imageUriList.add(data.getClipData().getItemAt(i).getUri());
                    previewImages.add(new SlideModel(data.getClipData().getItemAt(i).getUri().toString(), ScaleTypes.CENTER_INSIDE));
                }
            }
            else if(data.getData() != null)
            {
                imageUriList.add(data.getData());
                previewImages.add(new SlideModel(data.getData().toString(), ScaleTypes.CENTER_INSIDE));
            }

            imgView.setImageList(previewImages, ScaleTypes.FIT);
        }
    }

    public void uploadPicture(Uri imgUri)
    {
        final String randomKey= UUID.randomUUID().toString();
        final StorageReference storageRef = storageReference.child(timeStamp).child("images" + randomKey);

        storageRef.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        // Get a URL to the uploaded content
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                reff.child(timeStamp).child("images").push().setValue(new ImageUrl(uri.toString()));
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception exception)
                    {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
