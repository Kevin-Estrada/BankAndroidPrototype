package com.example.bankprototype2;

public class SaveUserData {

    public String userFirstName;
    public String userLastName;
    public String userEmailAddress;
    public String userPassword;
    public String userBalance;

    public SaveUserData(){

    }

    public SaveUserData(String userFirstName, String userLastName, String userEmailAddress, String userPassword, String userBalance) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmailAddress = userEmailAddress;
        this.userPassword = userPassword;
        this.userBalance = userBalance;
    }
}
