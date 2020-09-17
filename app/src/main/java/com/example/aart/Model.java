package com.example.aart;

public class Model {
   private int image;
   private String title;
   private String ageText;
   private String genderText;
   private String locationText;

    public Model(int image, String title, String age, String gender, String location) {
        this.image = image;
        this.title = title;
        this.ageText = age;
        this.genderText = gender;
        this.locationText= location;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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

