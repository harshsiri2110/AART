package com.petronus.petronus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdatePost extends AppCompatActivity {

    EditText txtTitle, txtAge, location, description, medical;
    Button btnupdate, selectImages;

    DatabaseReference reff, reff2;
    StorageReference storageReference;

    Spinner spinner;

    List<Uri> imageUriList = new ArrayList<>();

    String species, title, gender, location_text, age, desc, medical_text, timestamp;

    ArrayList<SlideModel> imageList = new ArrayList<>();

    ArrayList<ImageUrl> imgUrls = new ArrayList<>();

    RadioButton male, female, dog, cat, txtGender, txtSpecies;

    RadioGroup radioGenderGroup, radioSpeciesGroup;
    List<SlideModel> previewImages = new ArrayList<SlideModel>();

    ImageSlider imgView, newImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Post");

        if(!isConnected(this))
        {
            showCustomDialog();
        }

        final LoadingDialog loadingDialog = new LoadingDialog(UpdatePost.this);

        storageReference = FirebaseStorage.getInstance().getReference();

        description = (EditText) findViewById(R.id.update_postDescription);
        medical = (EditText) findViewById(R.id.update_medicalDetails);
        txtTitle = (EditText) findViewById(R.id.update_txttitle);
        txtAge = (EditText) findViewById(R.id.update_txtage);
        location = (EditText) findViewById(R.id.update_location);

        btnupdate = (Button) findViewById(R.id.update_btnupdate);
        selectImages = (Button) findViewById(R.id.update_selectImages);

        spinner = (Spinner)findViewById(R.id.update_dropdown_age);

        radioSpeciesGroup = findViewById(R.id.update_speciesSelect);
        radioGenderGroup = findViewById(R.id.update_radioGroup);

        male = findViewById(R.id.update_radioMale);
        female = findViewById(R.id.update_radioFemale);
        dog = findViewById(R.id.update_dogSelect);
        cat = findViewById(R.id.update_CatSelect);

        imgView = (ImageSlider) findViewById(R.id.update_imgView);
        newImgView = (ImageSlider) findViewById(R.id.update_imgViewAdd);

        Bundle bundle = getIntent().getExtras();

        species = bundle.getString("species");
        title = bundle.getString("title");
        age = bundle.getString("age");
        gender = bundle.getString("gender");
        location_text = bundle.getString("location");
        medical_text = bundle.getString("medical");
        desc = bundle.getString("desc");
        timestamp = bundle.getString("timestamp");

        String[] ageSplit = age.split(" ");

        description.setText(desc);
        medical.setText(medical_text);
        txtTitle.setText(title);
        txtAge.setText(ageSplit[0]);
        location.setText(location_text);


        if (species.equals("Dog")) {
            dog.toggle();
        } else {
            cat.toggle();
        }

        if (gender.equals("Male")) {
            male.toggle();
        } else {
            female.toggle();
        }

        selectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooser();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter <String> (UpdatePost.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Age));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        reff = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts").child(timestamp).child("images");
        reff2 = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts").child(timestamp);

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!isConnected(UpdatePost.this))
                {
                    showCustomDialog();
                }
                else {
                    for (DataSnapshot currImg : snapshot.getChildren()) {
                        ImageUrl tmp = currImg.getValue(ImageUrl.class);
                        imgUrls.add(tmp);
                        String uri = tmp.getUri();
                        imageList.add(new SlideModel(uri, ScaleTypes.CENTER_INSIDE));

                        if (imageList.size() == snapshot.getChildrenCount()) {
                            imgView.setImageList(imageList, ScaleTypes.FIT);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnected(UpdatePost.this))
                {
                    showCustomDialog();
                }
                else {
                    btnupdate.setEnabled(false);
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

                    selectedId = radioSpeciesGroup.getCheckedRadioButtonId();
                    txtSpecies = (RadioButton) findViewById(selectedId);
                    //model.setID(UUID.randomUUID().toString());

                    reff2.child("age").setValue(txtAge.getText().toString() + " " + spinner.getSelectedItem().toString());
                    reff2.child("description").setValue(description.getText().toString());
                    reff2.child("gender").setValue(txtGender.getText().toString());
                    reff2.child("location").setValue(location.getText().toString());
                    reff2.child("medical").setValue(medical.getText().toString());
                    reff2.child("speciesText").setValue(txtSpecies.getText().toString());
                    reff2.child("title").setValue(txtTitle.getText().toString());

                    startActivity(new Intent(getApplicationContext(), Foster_Profile.class));
                    if (!imageUriList.isEmpty()) {
                        for (int i = 0; i < imageUriList.size(); i++) {
                            uploadPicture(imageUriList.get(i));
                        }
                        finish();
                    }
                }
            }
        });

    }
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePost.this);
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

    private boolean isConnected(UpdatePost firstPage) {
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

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {
                    imageUriList.add(data.getClipData().getItemAt(i).getUri());
                    previewImages.add(new SlideModel(data.getClipData().getItemAt(i).getUri().toString(), ScaleTypes.CENTER_INSIDE));
                }
            } else if (data.getData() != null) {
                imageUriList.add(data.getData());
                previewImages.add(new SlideModel(data.getData().toString(), ScaleTypes.CENTER_INSIDE));
            }

            newImgView.setImageList(previewImages, ScaleTypes.FIT);
        }
    }

    public void uploadPicture(Uri imgUri) {
        final String randomKey = UUID.randomUUID().toString();
        final StorageReference storageRef = storageReference.child(timestamp).child("images" + randomKey);

        final DatabaseReference postsReff = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts");

        if(!isConnected(this))
        {
            showCustomDialog();
        }
        storageRef.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                postsReff.child(timestamp).child("images").push().setValue(new ImageUrl(uri.toString()));
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


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

