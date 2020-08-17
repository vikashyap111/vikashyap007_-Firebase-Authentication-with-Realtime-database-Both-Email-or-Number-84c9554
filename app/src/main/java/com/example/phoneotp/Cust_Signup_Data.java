package com.example.phoneotp;

public class Cust_Signup_Data {
    private String Username;
    private String Name;
    private String Email;
    private String Password;
    private String Number;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {return Username;}

    public void setUsername(String username) { this.Username = username; }


    public String getName() {return Name;}

    public void setName(String name) { this.Name = name; }

    public String getEmail() {return Email;}

    public void setEmail(String email) { this.Email = email; }

    public String getNumber() {return Number;}

    public void setNumber(String number) { this.Number = number;}

    public String getPassword() {return Password;}

    public void setPassword(String password) { this.Password = password;}



    Cust_Signup_Data(String Username, String Name, String Email, String Number, String Password) {
        this.Username = Username;
        this.Name = Name;

        this.Email = Email;

        this.Number = Number;
        this.Password = Password;
        //this.id = id;
    }
}
