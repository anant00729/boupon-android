package com.an2t.android.bouponapp.field_area.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anantawasthy on 11/14/17.
 */

public class MainAreaName {

    @SerializedName("areaNames")
    @Expose
    private List<String> areaNames = null;


    @SerializedName("areaCity")
    @Expose
    private List<String> areaCity = null;

    public List<String> getAreaCity() {
        return areaCity;
    }

    public void setAreaCity(List<String> areaCity) {
        this.areaCity = areaCity;
    }

    public List<String> getAreaNames() {
        return areaNames;
    }

    public void setAreaNames(List<String> areaNames) {
        this.areaNames = areaNames;
    }

}
