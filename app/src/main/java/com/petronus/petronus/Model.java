package com.petronus.petronus;

import java.util.ArrayList;

public class Model {

   private ArrayList<ImageUrl>images;
   private String title;
   private String description;
   private String ageText;
   private String genderText;
   private String locationText;
   private String speciesText;
   private String ID;
   private String medical;
   private String fosterEmail;
   private String fosterName;
   private long fosterNumber;

    public long getFosterNumber() {
        return fosterNumber;
    }

    public void setFosterNumber(long fosterNumber) {
        this.fosterNumber = fosterNumber;
    }

    public String getfosterEmail() {
        return fosterEmail;
    }

    public void setfosterEmail(String fosterEmail) {
        this.fosterEmail = fosterEmail;
    }

    public String getFosterName() {
        return fosterName;
    }

    public void setFosterName(String fosterName) {
        this.fosterName = fosterName;
    }

    public Model(ArrayList<ImageUrl> imageList, String title, String ageText, String genderText, String locationText) {
        this.images = imageList;
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
        images = new ArrayList<>();
        description= "";
        medical = "No medical history found :(";
        fosterEmail = "No foster found :(";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<ImageUrl> getImageList() {
        return images;
    }

    public void setImageList(ArrayList<ImageUrl> imageList) {
        this.images = imageList;
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

