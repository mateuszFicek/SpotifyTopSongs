package com.example.spotifytopsongs.Models;

public class Playlist {
    private String description;
    private String id;
    private String imageURL;
    private String name;

    public Playlist(String description, String id, String imageURL, String name) {
        this.description = description;
        this.id = id;
        this.imageURL = imageURL;
        this.name = name;
    }

    public Playlist(String description, String id, String name) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

