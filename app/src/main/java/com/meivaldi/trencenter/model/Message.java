package com.meivaldi.trencenter.model;

/**
 * Created by root on 26/08/18.
 */

public class Message {

    private int title, description, image;

    public Message(int title, int description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
