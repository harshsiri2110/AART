package com.example.aart;

public class Fosterdetails
{
    private String name;
    private String email;
    private String password;
    private long mobileno;

    public Fosterdetails(String name, String email, String password, long mobileno)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileno = mobileno;
    }

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

    public long getMobileno()
    {
        return mobileno;
    }

    public void setMobileno(long mobileno)
    {
        this.mobileno = mobileno;
    }
}
