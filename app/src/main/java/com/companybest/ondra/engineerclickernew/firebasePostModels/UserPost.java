package com.companybest.ondra.engineerclickernew.firebasePostModels;



public class UserPost {

    public String username;
    public String email;

    public UserPost() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserPost( String username, String email) {
        this.username = username;
        this.email = email;
    }

}