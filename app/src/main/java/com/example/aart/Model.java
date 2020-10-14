package com.example.aart;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Model {

   private List<ImageUrl>imageList;
   private String title;
   private String description;
   private String ageText;
   private String genderText;
   private String locationText;
   private String speciesText;
   private String ID;
   private String medical;
   private String fosterName;

    public String getFosterName() {
        return fosterName;
    }

    public void setFosterName(String fosterName) {
        this.fosterName = fosterName;
    }

    public Model(List<ImageUrl> imageList, String title, String ageText, String genderText, String locationText) {
        this.imageList = imageList;
        this.title = title;
        this.ageText = ageText;
        this.genderText = genderText;
        this.locationText = locationText;
    }

    public String getSpeciesText() {
        return speciesText;
    }

    public void setSpeciesText(String speciesText) {
        this.speciesText = speciesText;
    }

    public Model() {
        imageList = new ArrayList<>();
        description= "";
        medical = "No medical history found :(";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<ImageUrl> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageUrl> imageList) {
        this.imageList = imageList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
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

