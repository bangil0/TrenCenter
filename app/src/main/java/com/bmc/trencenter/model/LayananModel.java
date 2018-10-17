package com.bmc.trencenter.model;

/**
 * Created by root on 17/10/18.
 */

public class LayananModel {

    private String title, image;

    public LayananModel(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
