package com.tren.trencenter.model;

/**
 * Created by root on 12/09/18.
 */

public class Caleg {

    private String image, nama;

    public Caleg(String image, String nama) {
        this.image = image;
        this.nama = nama;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
