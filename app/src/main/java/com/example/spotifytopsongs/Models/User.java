package com.example.spotifytopsongs.Models;

/**
 * User class that could process user data from Spotify API.
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
