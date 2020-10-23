package com.example.aart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdapterFosterProfile extends BaseAdapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private int cardLayout;

    public AdapterFosterProfile(List<Model> models, Context context, int cardLayout) {
        this.models = models;
        this.context = context;
        this.cardLayout = cardLayout;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        layoutInflater = LayoutInflater.from(context);
        // View view = layoutInflater.inflate(R.layout.activity_cards,null);
        View view = layoutInflater.inflate(cardLayout,null);

        ImageSlider imageSlider;

        TextView title, ageText, genderText, locationText,updatePost,deletePost;

        final DatabaseReference dtbReff = FirebaseDatabase.getInstance().getReference().child("Foster").child("Posts");

        imageSlider = view.findViewById(R.id.image);

        title = view.findViewById(R.id.title);
        ageText = view.findViewById(R.id.ageText);
        genderText = view.findViewById(R.id.genderText);
        locationText = view.findViewById(R.id.locationText);
        updatePost = view.findViewById(R.id.card_update_post);
        deletePost = view.findViewById(R.id.card_delete_post);

        List<SlideModel> slideModels = new ArrayList<>();
        int count = 0;

        for (ImageUrl currUri : models.get(position).getImageList())
        {
            //Log.d("TEST","Inside Adapter!, Uri - "+currUri.getUri());
            slideModels.add(new SlideModel(currUri.getUri(), ScaleTypes.CENTER_INSIDE));
            count++;
            if(count >= 3)
            {
                break;
            }
        }

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        title.setText(models.get(position).getTitle());
        ageText.setText(models.get(position).getAge());
        genderText.setText(models.get(position).getGender());
        locationText.setText(models.get(position).getLocation());

        updatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,UpdatePost.class);
                intent.putExtra("species",models.get(position).getSpeciesText() );
                intent.putExtra("title",models.get(position).getTitle() );
                intent.putExtra("age",models.get(position).getAge() );
                intent.putExtra("gender",models.get(position).getGender() );
                intent.putExtra("location",models.get(position).getLocation() );
                intent.putExtra("desc",models.get(position).getDescription() );
                intent.putExtra("medical",models.get(position).getMedical() );
                intent.putExtra("timestamp",models.get(position).getID());

                context.startActivity(intent);
            }
        });

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dtbReff.child(models.get(position).getID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        context.startActivity(new Intent(context,Foster_Profile.class));
                    }
                });
            }
        });

        return view;
    }
}
