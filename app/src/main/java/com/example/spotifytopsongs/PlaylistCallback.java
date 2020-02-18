package com.example.spotifytopsongs;

import com.example.spotifytopsongs.Models.Playlist;
import com.example.spotifytopsongs.Models.Song;

import java.util.ArrayList;

public interface PlaylistCallback {
    void onSuccess(Playlist playlist);
}
