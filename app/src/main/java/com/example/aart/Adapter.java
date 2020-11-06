package com.example.aart;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private int cardLayout;

    public ImageSlider imageSlider;
    public TextView title, ageText, genderText, locationText;

    public Adapter(List<Model> models, Context context, int cardLayout) {
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

        imageSlider = view.findViewById(R.id.main_activity_image_slider);

        title = view.findViewById(R.id.title);
        ageText = view.findViewById(R.id.ageText);
        genderText = view.findViewById(R.id.genderText);
        locationText = view.findViewById(R.id.locationText);

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

        return view;
    }
}
