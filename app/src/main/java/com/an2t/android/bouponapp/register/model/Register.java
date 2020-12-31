package com.an2t.android.bouponapp.register.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anantawasthy on 8/22/17.
 */

public class Register {


    private String firstName;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;
    private String referralCodeUsed;

    public Register(String firstName,String email, String phoneNumber, String password, String confirmPassword, String referralCodeUsed) {
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.referralCodeUsed= referralCodeUsed;
    }


    public Register(String firstName, String email, String phoneNumber, String password, String confirmPassword) {
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    public class RegisterRes{
        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("token")
        @Expose
        private String token;

        public String getToken() {
            return token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
