package com.an2t.android.bouponapp.main.model;

/**
 * Created by anantawasthy on 7/3/17.
 */

public class InfoRestList {
    private String key;
    private String value;

    public InfoRestList() {
    }

    public InfoRestList(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
