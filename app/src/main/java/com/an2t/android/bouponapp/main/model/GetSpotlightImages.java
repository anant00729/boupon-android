package com.an2t.android.bouponapp.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by anantawasthy on 10/9/17.
 */

public class GetSpotlightImages {

    @SerializedName("items")
    @Expose
    private ArrayList<Item> items = null;

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public class Item implements Parcelable {
        @SerializedName("mainImage")
        @Expose
        private String mainImage;

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("longDescription")
        @Expose
        private String longDescription;


        @SerializedName("value")
        @Expose
        private String value;

        @SerializedName("expiry")
        @Expose
        private String expiry;

        @SerializedName("tnc")
        @Expose
        private String tnc;

        @SerializedName("category")
        @Expose
        private String category;

        @SerializedName("isFeatured")
        @Expose
        private Boolean isFeatured;


        public Boolean getFeatured() {
            return isFeatured;
        }

        public void setFeatured(Boolean featured) {
            isFeatured = featured;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLongDescription() {
            return longDescription;
        }

        public void setLongDescription(String longDescription) {
            this.longDescription = longDescription;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getTnc() {
            return tnc;
        }

        public void setTnc(String tnc) {
            this.tnc = tnc;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mainImage);
            dest.writeString(this.title);
            dest.writeString(this.longDescription);
            dest.writeString(this.value);
            dest.writeString(this.expiry);
            dest.writeString(this.tnc);
            dest.writeString(this.category);
            dest.writeValue(this.isFeatured);
        }

        public Item() {
        }

        protected Item(Parcel in) {
            this.mainImage = in.readString();
            this.title = in.readString();
            this.longDescription = in.readString();
            this.value = in.readString();
            this.expiry = in.readString();
            this.tnc = in.readString();
            this.category = in.readString();
            this.isFeatured = (Boolean) in.readValue(Boolean.class.getClassLoader());
        }

        public final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel source) {
                return new Item(source);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
    }
}
