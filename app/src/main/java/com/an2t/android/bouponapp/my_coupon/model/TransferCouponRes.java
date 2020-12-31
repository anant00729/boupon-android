package com.an2t.android.bouponapp.my_coupon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anantawasthy on 11/30/17.
 */

public class TransferCouponRes {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("auth")
    @Expose
    private Boolean auth;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
