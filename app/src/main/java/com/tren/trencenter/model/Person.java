package com.tren.trencenter.model;

/**
 * Created by root on 07/01/19.
 */

public class Person {

    private String image, nama, tipe;
    private int verified;

    public Person(String image, String nama, String tipe, int verified) {
        this.image = image;
        this.nama = nama;
        this.tipe = tipe;
        this.verified = verified;
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

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
}
