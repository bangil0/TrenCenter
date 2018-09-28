package com.meivaldi.trencenter.model;

/**
 * Created by root on 27/09/18.
 */
public class Layanan {
    private String image, nama, layanan;

    public Layanan(String image, String nama, String layanan) {
        this.image = image;
        this.nama = nama;
        this.layanan = layanan;
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

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }
}
