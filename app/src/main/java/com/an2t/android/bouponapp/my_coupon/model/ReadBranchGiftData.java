package com.an2t.android.bouponapp.my_coupon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anantawasthy on 11/29/17.
 */

public class ReadBranchGiftData {
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("type")
    @Expose
    private Integer type;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public class Data{

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("token")
        @Expose
        private String token;



        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }
}
