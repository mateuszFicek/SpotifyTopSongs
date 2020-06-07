package com.example.spotifytopsongs.Connectors;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifytopsongs.Models.Playlist;
import com.example.spotifytopsongs.Models.Song;
import com.example.spotifytopsongs.PlaylistCallback;
import com.example.spotifytopsongs.VolleyCallBack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PlaylistService {
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Playlist playlist;
    PlaylistCallback playlistCallback;

    public ArrayList<Playlist> getPlay() {
        return play;
    }

    private ArrayList<Playlist> play = new ArrayList<>();

    public PlaylistService(Context context, PlaylistCallback playlistCallback) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
        this.playlistCallback = playlistCallback;
    }

    public PlaylistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, endpoint, dataObject, response -> {
                    try {
                        Log.d("Respo", response.toString());
                        String description = response.getString("description");
                        String id = response.getString("id");
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

    public ArrayList<Playlist> getUserPlaylists(final VolleyCallBack callBack) {
        String user = sharedPreferences.getString("userid", "");
        String endpoint = "https://api.spotify.com/v1/users/" + user + "/playlists";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            String description = object.getString("description");
                            String id = object.getString("id");
                            String name = object.getString("name");
                            Playlist playlist = new Playlist(description, id, name);
                            play.add(playlist);
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
                headers.put("limit", "50");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return play;
    }

    public void addSongToPlaylist(Playlist playlist, Song song) {
        String endpoint = "https://api.spotify.com/v1/playlists/";
        endpoint += playlist.getId();
        endpoint += "/tracks";
        JSONArray tracksArray = new JSONArray();
        String name = "";
        name += "spotify:track:";
        name += song.getId();
        tracksArray.put(name);
        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("uris", (Object) tracksArray);
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, endpoint, dataObject, response -> {
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


    public void checkIfSongIsOnPlaylist(Context context,Playlist playlist, Song song) {
        String endpoint = "https://api.spotify.com/v1/playlists/" + playlist.getId() + "/tracks";
        ArrayList<String> songsOnPlaylist = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject object = jsonObject.getJSONObject("track");
                            String id = object.getString("id");
                            String name = object.getString("name");
                            songsOnPlaylist.add(id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!songsOnPlaylist.contains(song.getId())) {
                        addSongToPlaylist(playlist, song);
                        Toast.makeText(context, "Piosenka została dodana", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(context, "Piosenka już znajduje się na playliście.", Toast.LENGTH_SHORT).show();
                    }

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
