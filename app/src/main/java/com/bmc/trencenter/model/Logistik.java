package com.bmc.trencenter.model;

/**
 * Created by root on 11/09/18.
 */

public class Logistik {

    private String title, date, image, lokasi;

    public Logistik(String title, String date, String lokasi, String image) {
        this.title = title;
        this.date = date;
        this.image = image;
        this.lokasi = lokasi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
