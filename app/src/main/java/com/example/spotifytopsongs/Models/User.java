package com.example.spotifytopsongs.Models;

/**
 * Model class that is used to create and store data about single User.
 */
public class User {
    public String id;
    public String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public User(String id, String imageURL){
        this.id = id;
        this.imageURL = imageURL;
    }
}
