package com.example.spotifytopsongs.Connectors;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifytopsongs.Models.User;
import com.example.spotifytopsongs.VolleyCallBack;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;


/**
 * User Service that connects with Spotify API and gets all the data needed for further methods.
 * This class is responsible for getting token which allows developer to get user information.
 */
public class UserServices {
    private static final String ENDPOINT = "https://api.spotify.com/v1/me";
    private SharedPreferences msharedPreferences;
    private RequestQueue mqueue;
    private User user;
    private static final String clientID = "b597baec755a46d0934470305a5015de";
    private static final String clientSecret = "bc11fbf88ac141bc9379569355de2379";
    private static final String REDIRECT_URI = "com.example.spotifytopsongs://callback";

    public UserServices(RequestQueue queue, SharedPreferences sharedPreferences) {
        mqueue = queue;
        msharedPreferences = sharedPreferences;
    }

    public User getUser() {
        return user;
    }

    public void get(final VolleyCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, null, response -> {
            Gson gson = new Gson();
            user = gson.fromJson(response.toString(), User.class);
            callBack.onSuccess();
        }, error -> get(() -> {

        })) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = msharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        mqueue.add(jsonObjectRequest);
    }

}
