package com.example.spotifytopsongs.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifytopsongs.Models.Artist;
import com.example.spotifytopsongs.VolleyCallBack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Service used to get data from Spotify API about Artists.
 */
public class ArtistService {
    private ArrayList<Artist> topArtists = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public ArrayList<Artist> getTopArtists() {
        return topArtists;
    }

    public ArtistService(Context context){
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Getting array of artists that user listened to the most in short period of time (4 weeks)
     */
    public ArrayList<Artist> getTopArtistsFromSpotify(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists?time_range=short_term";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            String imageURL = object.getJSONArray("images").getJSONObject(1).getString("url");
                            String name = object.getString("name");
                            String id = object.getString("id");
                            String spotifyURL = object.getJSONObject("external_urls").getString("spotify");
                            Artist artist = new Artist(spotifyURL, id, imageURL, name);
                            topArtists.add(artist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return topArtists;
    }
}
