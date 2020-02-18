package com.example.spotifytopsongs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifytopsongs.Connectors.PlaylistService;
import com.example.spotifytopsongs.Models.Playlist;
import com.example.spotifytopsongs.Models.Song;
import com.example.spotifytopsongs.PlaylistCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Class which adds songs that are
 */
public class AddSongsOnCallback implements PlaylistCallback {
    private RequestQueue queue;
    private SharedPreferences sharedPreferences;
    private PlaylistService playlistService;
    Context context;
    ArrayList<Song> topsongs;
    AddSongsOnCallback(Context context, ArrayList<Song> topSongs){
        this.context = context;
        this.topsongs = topSongs;
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        playlistService = new PlaylistService(context, this);
        playlistService.createPlaylist();
        queue = Volley.newRequestQueue(context);
    }
    @Override
    public void onSuccess(Playlist playlist ) {
        String endpoint = "https://api.spotify.com/v1/playlists/";
        endpoint += playlist.getId();
        endpoint += "/tracks";
        JSONArray tracksArray = new JSONArray();
        Log.d("end", endpoint);
        String name = "";
        Log.d("SIZE", Integer.toString(topsongs.size()));
        for (int i = 0; i < topsongs.size(); i++) {
            name += "spotify:track:";
            name += topsongs.get(i).getId();
            tracksArray.put(name);
            Log.d("NAZWA", name);
            name = "";

        }
        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("uris", (Object) tracksArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, endpoint, dataObject, response -> {
                    Log.d("RESPONSE", response.toString());
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);

                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
