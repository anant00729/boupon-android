package com.an2t.android.bouponapp.recharge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anantawasthy on 9/25/17.
 */

public class InitiateRecharge {

    private String cellProvider;

    private String name;

    private String amount;

    private String email;

    private String phoneNumber;

    private String simCardType;

    private List<Deals> deals;


    public InitiateRecharge(String cellProvider, String name, String amount, String email, String phoneNumber, String simCardType, String userId,List<Deals> deals ) {
        this.cellProvider = cellProvider;
        this.name = name;
        this.amount = amount;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.simCardType = simCardType;
        this.userId = userId;
        this.deals = deals;
    }




    private String userId;

    public String getCellProvider() {
        return cellProvider;
    }

    public void setCellProvider(String cellProvider) {
        this.cellProvider = cellProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getSimCardType() {
        return simCardType;
    }

    public void setSimCardType(String simCardType) {
        this.simCardType = simCardType;
    }

    public List<Deals> getDeals() {
        return deals;
    }

    public void setDeals(List<Deals> deals) {
        this.deals = deals;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public static class Deals{

        private String dealId;
        private String sku;
        private String title;
        private String dealValue;
        private String image;
        private String expiry;

        public Deals(String dealId, String sku, String title, String dealValue, String image, String expiry) {
            this.dealId = dealId;
            this.sku = sku;
            this.title = title;
            this.dealValue = dealValue;
            this.image = image;
            this.expiry = expiry;
        }

        public String getDealId() {
            return dealId;
        }

        public void setDealId(String dealId) {
            this.dealId = dealId;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDealValue() {
            return dealValue;
        }

        public void setDealValue(String dealValue) {
            this.dealValue = dealValue;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }
    }

    public class InitiateRechargeRes{

        @SerializedName("paymentUrl")
        @Expose
        private String paymentUrl;
        @SerializedName("redirect_url")
        @Expose
        private String redirectUrl;

        public String getPaymentUrl() {
            return paymentUrl;
        }

        public void setPaymentUrl(String paymentUrl) {
            this.paymentUrl = paymentUrl;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }
    }
}
