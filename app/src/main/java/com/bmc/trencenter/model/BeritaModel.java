package com.bmc.trencenter.model;

/**
 * Created by root on 29/09/18.
 */
public class BeritaModel {

    private String title, foto;

    public BeritaModel(String title, String foto) {
        this.title = title;
        this.foto = foto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
