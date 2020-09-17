package com.example.aart;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

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


    public Adapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_cards,null);

        ImageSlider imageSlider;

        TextView title, ageText, genderText, locationText;

        imageSlider = view.findViewById(R.id.image);

        title = view.findViewById(R.id.title);
        ageText = view.findViewById(R.id.ageText);
        genderText = view.findViewById(R.id.genderText);
        locationText = view.findViewById(R.id.locationText);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.dog1, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.dog2, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.dog3, ScaleTypes.CENTER_INSIDE));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        title.setText(models.get(position).getTitle());
        ageText.setText(models.get(position).getAge());
        genderText.setText(models.get(position).getGender());
        locationText.setText(models.get(position).getLocation());

        return view;
    }



    /*
    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_cards, container,false);

        ImageSlider imageSlider;

        TextView title, ageText, genderText, locationText;

        imageSlider = view.findViewById(R.id.image);

        title = view.findViewById(R.id.title);
        ageText = view.findViewById(R.id.ageText);
        genderText = view.findViewById(R.id.genderText);
        locationText = view.findViewById(R.id.locationText);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.dog1, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.dog2, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.dog3, ScaleTypes.CENTER_INSIDE));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        title.setText(models.get(position).getTitle());
        ageText.setText(models.get(position).getAge());
        genderText.setText(models.get(position).getGender());
        locationText.setText(models.get(position).getLocation());


        container.addView(view, 0);


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
    */
}
