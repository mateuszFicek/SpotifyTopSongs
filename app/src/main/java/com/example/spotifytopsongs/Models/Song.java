package com.example.spotifytopsongs.Models;


/**
 * Class for managing song data.
 */
public class Song {

    private String id;
    private String name;
    private String artist;
    private String albumCoverURL;
    public Song(String id, String name) {
        this.name = name;
        this.id = id;
    }
    public Song(String id, String name, String artist) {
        this.name = name;
        this.id = id;
        this.artist = artist;
    }

    public Song(String id, String name, String artist, String albumCoverURL){
        this.name = name;
        this.id = id;
        this.albumCoverURL = albumCoverURL;
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAlbumCoverURL() {
        return albumCoverURL;
    }

    public void setAlbumCoverURL(String albumCoverURL) {
        this.albumCoverURL = albumCoverURL;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}