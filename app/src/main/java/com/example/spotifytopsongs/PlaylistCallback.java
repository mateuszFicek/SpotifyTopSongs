package com.example.spotifytopsongs;

import com.example.spotifytopsongs.Models.Playlist;

/**
 * Callback function for Playlists.
 */
public interface PlaylistCallback {
    void onSuccess(Playlist playlist);
}
