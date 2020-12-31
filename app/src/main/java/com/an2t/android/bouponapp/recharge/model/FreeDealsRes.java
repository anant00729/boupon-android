package com.an2t.android.bouponapp.recharge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by anantawasthy on 9/29/17.
 */

public class FreeDealsRes {

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("simcardType")
    @Expose
    private String simcardType;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("redeemAmount")
    @Expose
    private String redeemAmount;
    @SerializedName("items")
    @Expose
    private ArrayList<Item> items = null;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSimcardType() {
        return simcardType;
    }

    public void setSimcardType(String simcardType) {
        this.simcardType = simcardType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRedeemAmount() {
        return redeemAmount;
    }

    public void setRedeemAmount(String redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public static class Item implements Parcelable {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("parentStoreId")
        @Expose
        private ParentStoreId parentStoreId;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("rank")
        @Expose
        private Integer rank;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("subTitle")
        @Expose
        private String subTitle;
        @SerializedName("shortDescription")
        @Expose
        private String shortDescription;
        @SerializedName("longDescription")
        @Expose
        private String longDescription;
        @SerializedName("highlights")
        @Expose
        private String highlights;
        @SerializedName("location")
        @Expose
        private List<Double> location = null;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("expiry")
        @Expose
        private String expiry;

        @SerializedName("mainImage")
        @Expose
        private String mainImage;

        @SerializedName("originalPrice")
        @Expose
        private String originalPrice;

        @SerializedName("discountedPrice")
        @Expose
        private String discountedPrice;

        @SerializedName("value")
        @Expose
        private String value;

        @SerializedName("tnc")
        @Expose
        private String tnc;

        @SerializedName("areaName")
        @Expose
        private String areaName;

        @SerializedName("city")
        @Expose
        private String city;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        private String distance;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getTnc() {
            return tnc;
        }

        public void setTnc(String tnc) {
            this.tnc = tnc;
        }

        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getDiscountedPrice() {
            return discountedPrice;
        }

        public void setDiscountedPrice(String discountedPrice) {
            this.discountedPrice = discountedPrice;
        }

        private String position;

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ParentStoreId getParentMerchantId() {
            return parentStoreId;
        }

        public void setParentMerchantId(ParentStoreId parentStoreId) {
            this.parentStoreId = parentStoreId;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getLongDescription() {
            return longDescription;
        }

        public void setLongDescription(String longDescription) {
            this.longDescription = longDescription;
        }

        public String getHighlights() {
            return highlights;
        }

        public void setHighlights(String highlights) {
            this.highlights = highlights;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }


        @Override
        public String toString() {
            return "Item{" +
                    "id='" + id + '\'' +
                    ", parentStoreId=" + parentStoreId +
                    ", sku='" + sku + '\'' +
                    ", rank=" + rank +
                    ", title='" + title + '\'' +
                    ", subTitle='" + subTitle + '\'' +
                    ", shortDescription='" + shortDescription + '\'' +
                    ", longDescription='" + longDescription + '\'' +
                    ", highlights='" + highlights + '\'' +
                    ", location=" + location +
                    ", category='" + category + '\'' +
                    ", expiry='" + expiry + '\'' +
                    ", mainImage='" + mainImage + '\'' +
                    ", originalPrice='" + originalPrice + '\'' +
                    ", discountedPrice='" + discountedPrice + '\'' +
                    ", value='" + value + '\'' +
                    ", tnc='" + tnc + '\'' +
                    ", areaName='" + areaName + '\'' +
                    ", city='" + city + '\'' +
                    ", distance='" + distance + '\'' +
                    ", isSelected=" + isSelected +
                    ", position='" + position + '\'' +
                    '}';
        }

        public static Comparator<Item> distanceComparator = new Comparator<Item>() {
            @Override
            public int compare(Item itemOne, Item itemTwo) {
                if (Double.parseDouble(itemOne.getDistance()) < Double.parseDouble(itemTwo.getDistance())) return -1;
                if (Double.parseDouble(itemOne.getDistance()) > Double.parseDouble(itemTwo.getDistance())) return 1;
                return 0;
            }
        };



        public class ParentStoreId implements Parcelable {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("parentMerchantId")
            @Expose
            private String parentMerchantId;
            @SerializedName("storeName")
            @Expose
            private String storeName;
            @SerializedName("storeEmail")
            @Expose
            private String storeEmail;
            @SerializedName("storePhoneNumber")
            @Expose
            private String storePhoneNumber;
            @SerializedName("storeAddress")
            @Expose
            private String storeAddress;
            @SerializedName("storeWebsite")
            @Expose
            private String storeWebsite;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getParentMerchantId() {
                return parentMerchantId;
            }

            public void setParentMerchantId(String parentMerchantId) {
                this.parentMerchantId = parentMerchantId;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public String getStoreEmail() {
                return storeEmail;
            }

            public void setStoreEmail(String storeEmail) {
                this.storeEmail = storeEmail;
            }

            public String getStorePhoneNumber() {
                return storePhoneNumber;
            }

            public void setStorePhoneNumber(String storePhoneNumber) {
                this.storePhoneNumber = storePhoneNumber;
            }

            public String getStoreAddress() {
                return storeAddress;
            }

            public void setStoreAddress(String storeAddress) {
                this.storeAddress = storeAddress;
            }

            public String getStoreWebsite() {
                return storeWebsite;
            }

            public void setStoreWebsite(String storeWebsite) {
                this.storeWebsite = storeWebsite;
            }



            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.parentMerchantId);
                dest.writeString(this.storeName);
                dest.writeString(this.storeEmail);
                dest.writeString(this.storePhoneNumber);
                dest.writeString(this.storeAddress);
//                dest.writeString(this.storeWebsite);

            }

            public ParentStoreId() {
            }

            protected ParentStoreId(Parcel in) {
                this.id = in.readString();
                this.parentMerchantId = in.readString();
                this.storeName = in.readString();
                this.storeEmail = in.readString();
                this.storePhoneNumber = in.readString();
                this.storeAddress = in.readString();
                /*this.storeWebsite = in.readString();*/

            }

            public final Creator<ParentStoreId> CREATOR = new Creator<ParentStoreId>() {
                @Override
                public ParentStoreId createFromParcel(Parcel source) {
                    return new ParentStoreId(source);
                }

                @Override
                public ParentStoreId[] newArray(int size) {
                    return new ParentStoreId[size];
                }
            };
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeParcelable(this.parentStoreId, flags);
            dest.writeString(this.sku);
            dest.writeValue(this.rank);
            dest.writeString(this.title);
            dest.writeString(this.subTitle);
            dest.writeString(this.shortDescription);
            dest.writeString(this.longDescription);
            dest.writeString(this.highlights);
            dest.writeList(this.location);
            dest.writeString(this.category);
            dest.writeString(this.expiry);
            dest.writeString(this.mainImage);
            dest.writeString(this.originalPrice);
            dest.writeString(this.discountedPrice);
            dest.writeString(this.value);
            dest.writeString(this.tnc);
            dest.writeString(this.areaName);
            dest.writeString(this.city);
            dest.writeString(this.distance);
            dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
            dest.writeString(this.position);
        }

        public Item() {
        }

        protected Item(Parcel in) {
            this.id = in.readString();
            this.parentStoreId = in.readParcelable(ParentStoreId.class.getClassLoader());
            this.sku = in.readString();
            this.rank = (Integer) in.readValue(Integer.class.getClassLoader());
            this.title = in.readString();
            this.subTitle = in.readString();
            this.shortDescription = in.readString();
            this.longDescription = in.readString();
            this.highlights = in.readString();
            this.location = new ArrayList<Double>();
            in.readList(this.location, Double.class.getClassLoader());
            this.category = in.readString();
            this.expiry = in.readString();
            this.mainImage = in.readString();
            this.originalPrice = in.readString();
            this.discountedPrice = in.readString();
            this.value = in.readString();
            this.tnc = in.readString();
            this.areaName = in.readString();
            this.city = in.readString();
            this.distance = in.readString();
            this.isSelected = in.readByte() != 0;
            this.position = in.readString();
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
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
