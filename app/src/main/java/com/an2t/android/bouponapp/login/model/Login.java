package com.an2t.android.bouponapp.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anantawasthy on 8/22/17.
 */

public class Login {

    private String phoneNumber;
    private String password;


    public Login(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getEmail() {
        return phoneNumber;
    }

    public void setEmail(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public class LoginRes{
        @SerializedName("message")
        @Expose
        private Message message;



        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
        public class Message {

            @SerializedName("text")
            @Expose
            private String text;
            @SerializedName("token")
            @Expose
            private String token;
            @SerializedName("payload")
            @Expose
            private Payload payload;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public Payload getPayload() {
                return payload;
            }

            public void setPayload(Payload payload) {
                this.payload = payload;
            }

        }

        public class Payload {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("userType")
            @Expose
            private String userType;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("firstName")
            @Expose
            private String firstName;
            @SerializedName("lastName")
            @Expose
            private String lastName;
            @SerializedName("phoneNumber")
            @Expose
            private String phoneNumber;
            @SerializedName("otpVerificationStatus")
            @Expose
            private Boolean otpVerificationStatus;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public Boolean getOtpVerificationStatus() {
                return otpVerificationStatus;
            }

            public void setOtpVerificationStatus(Boolean otpVerificationStatus) {
                this.otpVerificationStatus = otpVerificationStatus;
            }

        }
    }
}
