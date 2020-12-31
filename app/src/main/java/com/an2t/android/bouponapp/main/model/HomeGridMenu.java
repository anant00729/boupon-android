package com.an2t.android.bouponapp.main.model;

/**
 * Created by anantawasthy on 9/4/17.
 */

public class HomeGridMenu {
    private String name;
    private String image;

    public HomeGridMenu(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
