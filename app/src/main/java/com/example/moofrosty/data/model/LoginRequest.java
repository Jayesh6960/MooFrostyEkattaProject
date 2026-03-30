package com.example.moofrosty.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email") // Ensures JSON key is "email"
    private String email;

//    @SerializedName("mobilenumber") // Ensures JSON key is "email"
//    private String mobilenumber;

    @SerializedName("password") // Ensures JSON key is "password"
    private String password;
    public LoginRequest(String email, String password) {
//        this.mobilenumber=mobilenumber;
        this.email = email;
        this.password = password;
    }
}
