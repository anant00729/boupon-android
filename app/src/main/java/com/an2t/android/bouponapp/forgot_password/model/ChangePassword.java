package com.an2t.android.bouponapp.forgot_password.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anantawasthy on 10/17/17.
 */

public class ChangePassword {
    private String resetPasswordToken;
    private String password;
    private String confirmPassword;

    public ChangePassword(String resetPasswordToken, String password, String confirmPassword) {
        this.resetPasswordToken = resetPasswordToken;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
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

    public class ChangePasswordRes{
        @SerializedName("message")
        @Expose
        private Message message;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public class Message{

            @SerializedName("message")
            @Expose
            private String message;
            @SerializedName("token")
            @Expose
            private String token;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

        }
    }
}
