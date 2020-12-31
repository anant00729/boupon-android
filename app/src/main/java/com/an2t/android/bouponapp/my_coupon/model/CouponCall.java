package com.an2t.android.bouponapp.my_coupon.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by anantawasthy on 10/11/17.
 */

public class CouponCall {
    private String userId;
    private String code;

    public CouponCall(String code,String userId) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CouponCall(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public class CouponRes{

        @SerializedName("host")
        @Expose
        private String host;
        @SerializedName("coupons")
        @Expose
        private ArrayList<Coupon> coupons = null;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public ArrayList<Coupon> getCoupons() {
            return coupons;
        }

        public void setCoupons(ArrayList<Coupon> coupons) {
            this.coupons = coupons;
        }
        public class Coupon implements Parcelable {

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
            @SerializedName("shareToken")
            @Expose
            private String shareToken;
            @SerializedName("_id")
            @Expose
            private String _id;


            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getShareToken() {
                return shareToken;
            }

            public void setShareToken(String shareToken) {
                this.shareToken = shareToken;
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

            public Boolean getRedeemed() {
                return isRedeemed;
            }

            public void setRedeemed(Boolean redeemed) {
                isRedeemed = redeemed;
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


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
                dest.writeString(this.code);
                dest.writeValue(this.amount);
                dest.writeString(this.image);
                dest.writeString(this.customerId);
                dest.writeString(this.sku);
                dest.writeString(this.orderID);
                dest.writeValue(this.isRedeemed);
                dest.writeString(this.updatedAt);
                dest.writeString(this.createdAt);
                dest.writeString(this.expiry);
                dest.writeString(this.shareToken);
                dest.writeString(this._id);
            }

            public Coupon() {
            }

            protected Coupon(Parcel in) {
                this.name = in.readString();
                this.code = in.readString();
                this.amount = (Integer) in.readValue(Integer.class.getClassLoader());
                this.image = in.readString();
                this.customerId = in.readString();
                this.sku = in.readString();
                this.orderID = in.readString();
                this.isRedeemed = (Boolean) in.readValue(Boolean.class.getClassLoader());
                this.updatedAt = in.readString();
                this.createdAt = in.readString();
                this.expiry = in.readString();
                this.shareToken = in.readString();
                this._id = in.readString();
            }

            public final Creator<Coupon> CREATOR = new Creator<Coupon>() {
                @Override
                public Coupon createFromParcel(Parcel source) {
                    return new Coupon(source);
                }

                @Override
                public Coupon[] newArray(int size) {
                    return new Coupon[size];
                }
            };

            @Override
            public String toString() {
                return "Coupon{" +
                        "name='" + name + '\'' +
                        ", code='" + code + '\'' +
                        ", amount=" + amount +
                        ", image='" + image + '\'' +
                        ", customerId='" + customerId + '\'' +
                        ", sku='" + sku + '\'' +
                        ", orderID='" + orderID + '\'' +
                        ", isRedeemed=" + isRedeemed +
                        ", updatedAt='" + updatedAt + '\'' +
                        ", createdAt='" + createdAt + '\'' +
                        ", expiry='" + expiry + '\'' +
                        ", shareToken='" + shareToken + '\'' +
                        ", _id='" + _id + '\'' +
                        ", CREATOR=" + CREATOR +
                        '}';
            }
        }
    }
}
