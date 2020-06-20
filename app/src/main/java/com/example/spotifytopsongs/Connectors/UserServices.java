package com.example.spotifytopsongs.Connectors;

import android.content.SharedPreferences;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.spotifytopsongs.Models.User;
import com.example.spotifytopsongs.VolleyCallBack;
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

    public UserServices(RequestQueue queue, SharedPreferences sharedPreferences) {
        mqueue = queue;
        msharedPreferences = sharedPreferences;
    }

    public User getUser() {
        return user;
    }

    public void get(final VolleyCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, null, response -> {
            try {
                String id = response.getString("id");
                String imageURL = "";
                if(response.getJSONArray("images").length() > 0)
                    response.getJSONArray("images").getJSONObject(0).getString("url");

                this.user = new User(id, imageURL);
            } catch (Exception e){
                e.printStackTrace();
            }
            callBack.onSuccess();
        }, error -> get(() -> {

        })) {
            @Override
            public Map<String, String> getHeaders() {
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
