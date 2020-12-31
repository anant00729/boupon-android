package com.an2t.android.bouponapp.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by anantawasthy on 10/10/17.
 */

public class RechargeHistory {


    public static class RechargeHistorySend{
        private String userId;
        private String email;

        public RechargeHistorySend(String userId, String email) {
            this.userId = userId;
            this.email = email;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @SerializedName("rechageHistory")
    @Expose
    private ArrayList<RechageHistory> rechageHistory = null;

    public ArrayList<RechageHistory> getRechageHistory() {
        return rechageHistory;
    }

    public void setRechageHistory(ArrayList<RechageHistory> rechageHistory) {
        this.rechageHistory = rechageHistory;
    }
    public static class RechageHistory{
        @SerializedName("customerId")
        @Expose
        private String customerId;
        @SerializedName("rechargeType")
        @Expose
        private String rechargeType;
        @SerializedName("customerName")
        @Expose
        private String customerName;
        @SerializedName("mobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("orderID")
        @Expose
        private String orderID;
        @SerializedName("provider")
        @Expose
        private String provider;
        @SerializedName("invoice")
        @Expose
        private String invoice;
        @SerializedName("deals")
        @Expose
        private ArrayList<Deal> deals = null;

        @SerializedName("transactionID")
        @Expose
        private String transactionID;

        @SerializedName("paymentStatus")
        @Expose
        private String paymentStatus;



        @SerializedName("timeAdded")
        @Expose
        private String timeAdded;

        public String getTimeAdded() {
            return timeAdded;
        }

        public void setTimeAdded(String timeAdded) {
            this.timeAdded = timeAdded;
        }

        public String getTransactionID() {
            return transactionID;
        }

        public void setTransactionID(String transactionID) {
            this.transactionID = transactionID;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getRechargeType() {
            return rechargeType;
        }

        public void setRechargeType(String rechargeType) {
            this.rechargeType = rechargeType;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public ArrayList<Deal> getDeals() {
            return deals;
        }

        public void setDeals(ArrayList<Deal> deals) {
            this.deals = deals;
        }

        public class Deal{
            @SerializedName("dealSku")
            @Expose
            private String dealSku;
            @SerializedName("dealTitle")
            @Expose
            private String dealTitle;
            @SerializedName("dealValue")
            @Expose
            private Integer dealValue;
            @SerializedName("dealImage")
            @Expose
            private String dealImage;
            @SerializedName("dealExpiry")
            @Expose
            private String dealExpiry;

            public String getDealSku() {
                return dealSku;
            }

            public void setDealSku(String dealSku) {
                this.dealSku = dealSku;
            }

            public String getDealTitle() {
                return dealTitle;
            }

            public void setDealTitle(String dealTitle) {
                this.dealTitle = dealTitle;
            }

            public Integer getDealValue() {
                return dealValue;
            }

            public void setDealValue(Integer dealValue) {
                this.dealValue = dealValue;
            }

            public String getDealImage() {
                return dealImage;
            }

            public void setDealImage(String dealImage) {
                this.dealImage = dealImage;
            }

            public String getDealExpiry() {
                return dealExpiry;
            }

            public void setDealExpiry(String dealExpiry) {
                this.dealExpiry = dealExpiry;
            }

        }

    }
}
