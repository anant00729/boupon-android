package com.an2t.android.bouponapp.payment.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anantawasthy on 9/28/17.
 */

public class RechargeAmountMobile implements Serializable {

    private String amount;
    private String rechargeStatus;
    private String mobileNumber;
    private String paymentStatus;
    private String message;
    private String transactionID;
    private List<Coupon> mCoupons;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public List<Coupon> getmCoupons() {
        return mCoupons;
    }

    public void setmCoupons(List<Coupon> mCoupons) {
        this.mCoupons = mCoupons;
    }

    public class Coupon implements Serializable{

    }
}
