package com.an2t.android.bouponapp.my_coupon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anantawasthy on 10/11/17.
 */

public class RedeemCoupon {
    private String code;

    public RedeemCoupon(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public class RedeemCouponRes{
        @SerializedName("coupon")
        @Expose
        private Coupon coupon;
        @SerializedName("isExpired")
        @Expose
        private Boolean isExpired;
        @SerializedName("message")
        @Expose
        private String message;

        public Coupon getCoupon() {
            return coupon;
        }

        public void setCoupon(Coupon coupon) {
            this.coupon = coupon;
        }

        public Boolean getIsExpired() {
            return isExpired;
        }

        public void setIsExpired(Boolean isExpired) {
            this.isExpired = isExpired;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public class Coupon{
            @SerializedName("merchantId")
            @Expose
            private String merchantId;
            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("__v")
            @Expose
            private Integer v;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("code")
            @Expose
            private String code;
            @SerializedName("amount")
            @Expose
            private Integer amount;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("customerId")
            @Expose
            private String customerId;
            @SerializedName("sku")
            @Expose
            private String sku;
            @SerializedName("orderID")
            @Expose
            private String orderID;
            @SerializedName("isRedeemed")
            @Expose
            private Boolean isRedeemed;
            @SerializedName("updatedAt")
            @Expose
            private String updatedAt;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("expiry")
            @Expose
            private String expiry;
            @SerializedName("start")
            @Expose
            private String start;

            public String getMerchantId() {
                return merchantId;
            }

            public void setMerchantId(String merchantId) {
                this.merchantId = merchantId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Integer getV() {
                return v;
            }

            public void setV(Integer v) {
                this.v = v;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public Integer getAmount() {
                return amount;
            }

            public void setAmount(Integer amount) {
                this.amount = amount;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }



            public String getSku() {
                return sku;
            }

            public void setSku(String sku) {
                this.sku = sku;
            }

            public String getOrderID() {
                return orderID;
            }

            public void setOrderID(String orderID) {
                this.orderID = orderID;
            }

            public Boolean getIsRedeemed() {
                return isRedeemed;
            }

            public void setIsRedeemed(Boolean isRedeemed) {
                this.isRedeemed = isRedeemed;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getExpiry() {
                return expiry;
            }

            public void setExpiry(String expiry) {
                this.expiry = expiry;
            }

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
            }

        }
    }
}
