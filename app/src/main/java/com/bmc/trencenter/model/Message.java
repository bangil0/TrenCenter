package com.bmc.trencenter.model;

/**
 * Created by root on 26/08/18.
 */

public class Message {

    private String title, date, image;

    public Message(String title, String date, String image) {
        this.title = title;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
