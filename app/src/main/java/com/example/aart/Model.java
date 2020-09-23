package com.example.aart;

import android.net.Uri;

import java.util.List;

public class Model {
   private List<Uri> image;
   private int imageLocal;
   private String title;
   private String ageText;
   private String genderText;
   private String locationText;

    public Model(List<Uri> image, String title, String age, String gender, String location) {
        this.image = image;
        this.title = title;
        this.ageText = age;
        this.genderText = gender;
        this.locationText= location;
    }

    public Model(int image, String title, String age, String gender, String location) {
        this.imageLocal = image;
        this.title = title;
        this.ageText = age;
        this.genderText = gender;
        this.locationText= location;
    }

    public Model() {
    }

    public int getImageLocal() {
        return imageLocal;
    }

    public void Local(int image) {
        this.imageLocal = image;
    }

    public List<Uri> getImage() {
        return image;
    }

    public void setImage(List<Uri> image) {
        this.image = image;
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

