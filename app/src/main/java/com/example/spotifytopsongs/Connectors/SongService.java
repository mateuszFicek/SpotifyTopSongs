package com.example.spotifytopsongs.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifytopsongs.Models.Playlist;
import com.example.spotifytopsongs.Models.Song;
import com.example.spotifytopsongs.PlaylistCallback;
import com.example.spotifytopsongs.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class which sends request to Spotify API and manages obtained json results.
 * This class is responsible for getting information about users currently listening songs, recently listened and top songs from short_term period.
 */
public class SongService {
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Song> topSongs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Song currentSong;
    private Playlist playlist;
    private SharedPreferences.Editor editor;


    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public ArrayList<Song> getTopSongs() {
        return topSongs;
    }

    public Song getSong() {
        return currentSong;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * Getting array of songs that user recently listened to.
     */

    public ArrayList<Song> getRecentlyPlayedTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            object = object.optJSONObject("track");
                            Song song = gson.fromJson(object.toString(), Song.class);
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
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
        return songs;
    }

    /**
     * Getting current song that user is listening to.
     */
    public Song getCurrentSong(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/currently-playing";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONObject jsonObject = response.optJSONObject("item");
                    try {
                        String name = jsonObject.getString("name");
                        String id = jsonObject.getString("id");
                        Song song = new Song(id, name);
                        currentSong = song;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callBack.onSuccess();
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
        return currentSong;
    }

    /**
     * Getting array of songs that user listened to the most in short period of time (4 weeks)
     */
    public ArrayList<Song> getTopSongsFromSpotify(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/top/tracks";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            String coverURL = object.getJSONObject("album").getJSONArray("images").getJSONObject(1).getString("url");
                            String name = object.getString("name");
                            String id = object.getString("id");
                            JSONArray artist = object.getJSONArray("artists");
                            String artists = new String();
                            for (int a = 0; a < artist.length(); a++) {
                                JSONObject current = artist.getJSONObject(a);
                                if (a == 0) {
                                    artists += current.getString("name");
                                } else {
                                    artists += (", ");
                                    artists += current.getString("name");
                                }
                            }
                            Song song = new Song(id, name, artists, coverURL);
                            topSongs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("time_range", "short_term");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return topSongs;
    }


}