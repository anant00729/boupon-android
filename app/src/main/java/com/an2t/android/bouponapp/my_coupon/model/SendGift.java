package com.an2t.android.bouponapp.my_coupon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anantawasthy on 11/27/17.
 */

public class SendGift {


    private String phoneNumber;
    private String code;
    private String couponId;
    private String giftCouponUrl;

    public SendGift(String phoneNumber, String code, String couponId, String giftCouponUrl) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.couponId = couponId;
        this.giftCouponUrl = giftCouponUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getCode() {
        return code;
    }
    public String getCouponId() {
        return couponId;
    }
    public String getGiftCouponUrl() {
        return giftCouponUrl;
    }

    public class SendGiftRes{
        @SerializedName("status")
        @Expose
        private boolean status;
        @SerializedName("message")
        @Expose
        private String message;

        public boolean isStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

}
