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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Callback class for creatingPlaylist and when it is successful it passes playlist to AddSongsOnCallback class.
 */
public class PlaylistService {
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Song> topSongs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Song currentSong;
    private Playlist playlist;
    private SharedPreferences.Editor editor;
    PlaylistCallback playlistCallback;

    public PlaylistService(Context context, PlaylistCallback playlistCallback) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
        this.playlistCallback = playlistCallback;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void createPlaylist() {
        String endpoint = "https://api.spotify.com/v1/users/";
        String user = sharedPreferences.getString("userid", "");
        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("name", "Your Top Songs of this Month");
            dataObject.put("description", "Your current top songs.");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        endpoint += user;
        endpoint += "/playlists";
        Log.d("ENDPOINT", endpoint);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, endpoint, dataObject, response -> {
                    try {
                        Log.d("Respo", response.toString());
                        String description = response.getString("description");
                        String id = response.getString("id");
                        Log.d("PLAYLISTID", id);
                        String name = response.getString("name");
                        playlist = new Playlist(description, id, name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    playlistCallback.onSuccess(playlist);
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
