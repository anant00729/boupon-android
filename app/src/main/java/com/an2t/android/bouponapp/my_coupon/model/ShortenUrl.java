package com.an2t.android.bouponapp.my_coupon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anantawasthy on 11/26/17.
 */

public class ShortenUrl {

    private String url;

    public ShortenUrl(String url) {
        this.url = url;
    }

    public String getShortUrl() {
        return url;
    }

    public void setShortUrl(String shortUrl) {
        this.url = url;
    }


    public class ShortenUrlRes{

        @SerializedName("branchUrl")
        @Expose
        private String branchUrl;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getShortUrl() {
            return branchUrl;
        }

        public void setShortUrl(String branchUrl) {
            this.branchUrl = branchUrl;
        }
    }
}
