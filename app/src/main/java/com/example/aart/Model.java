package com.example.aart;

import android.net.Uri;

import java.util.List;

public class Model {

   private int imageLocal;
   private String title;
   private String description;
   private String ageText;
   private String genderText;
   private String locationText;


    public Model(int image, String title, String age, String gender, String location, String description) {
        this.imageLocal = image;
        this.title = title;
        this.ageText = age;
        this.genderText = gender;
        this.locationText= location;
        this.description = description;

    }

    public Model() {
    }

    public int getImageLocal()
    {
        return imageLocal;
    }

    public void setLocal(int image)
    {
        this.imageLocal = image;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAge() {
        return ageText;
    }

    public void setAge(String age) {
        this.ageText = age;
    }

    public String getGender() {
        return genderText;
    }

    public void setGender(String gender) {
        this.genderText = gender;
    }

    public String getLocation() {
        return locationText;
    }

    public void setLocation(String location) {
        this.locationText = location;
    }
}

