package com.an2t.android.bouponapp.recharge.model;

/**
 * Created by anantawasthy on 9/26/17.
 */

public class RechargeHistory {

    private String name;
    private String phone;

    public RechargeHistory(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
