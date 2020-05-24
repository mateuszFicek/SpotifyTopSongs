package com.example.spotifytopsongs.Models;

public class Artist {
    public String getSpotifyURL() {
        return spotifyURL;
    }

    public void setSpotifyURL(String spotifyURL) {
        this.spotifyURL = spotifyURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String spotifyURL;
    private String id;
    private String imageURL;
    private String name;

    public Artist(String spotifyURL, String id, String imageURL, String name){
        this.id = id;
        this.imageURL = imageURL;
        this.spotifyURL = spotifyURL;
        this.name = name;
    }
}
