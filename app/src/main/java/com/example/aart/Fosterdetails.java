package com.example.aart;

public class Fosterdetails
{
    private String name;
    private String email;
    private String password;
    private long mobileNo;
    private int profilePic;

    public Fosterdetails(String name, String email, long mobileNo, int profilePic)
    {
        this.name = name;
        this.email = email;
        this.mobileNo = mobileNo;
        this.profilePic = profilePic;
    }

    public Fosterdetails(String name, String email, String password, long mobileNo, int profilePic)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNo;
        this.profilePic = profilePic;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    public Fosterdetails() { }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public long getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(long mobileNo)
    {
        this.mobileNo = mobileNo;
    }
}
