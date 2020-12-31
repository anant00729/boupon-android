package com.an2t.android.bouponapp.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anantawasthy on 11/28/17.
 */

public class VersionCodeRes {

    @SerializedName("versionCode")
    @Expose
    private String versionCode;

    public String getVersionCode() {
        return versionCode;
    }
}
