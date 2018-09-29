package com.meivaldi.trencenter.model;

/**
 * Created by root on 29/09/18.
 */

public class LogisticReport {

    private String name, total, image;

    public LogisticReport(String name, String total, String image) {
        this.name = name;
        this.total = total;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
