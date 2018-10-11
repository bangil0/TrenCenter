package com.bmc.trencenter.model;

/**
 * Created by root on 28/08/18.
 */

public class Program {

    private String title, date, location, image;

    public Program(String title, String date, String location, String image) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
