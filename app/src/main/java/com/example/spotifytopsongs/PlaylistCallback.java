package com.example.spotifytopsongs;

import com.example.spotifytopsongs.Models.Playlist;
import com.example.spotifytopsongs.Models.Song;

import java.util.ArrayList;

/**
 * Callback function for Playlists.
 */
public interface PlaylistCallback {
    void onSuccess(Playlist playlist);
}
