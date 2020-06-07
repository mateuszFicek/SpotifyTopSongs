package com.example.spotifytopsongs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.spotifytopsongs.Models.User;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.example.spotifytopsongs.Connectors.UserServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Start Activity, authenticate Spotify
 */
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private RequestQueue queue;

    private static final String CLIENT_ID = "b597baec755a46d0934470305a5015de";
    private static final String REDIRECT_URI = "com.example.spotifytopsongs://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String[] SCOPES = {"user-read-private, user-read-currently-playing, user-top-read, playlist-modify-public, playlist-modify-private", "streaming"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        prefs = this.getSharedPreferences(
                "SPOTIFY", 0);
        String currentDate = new SimpleDateFormat("w", Locale.getDefault()).format(new Date());
        String value = prefs.getString("DATE", "none");
        boolean shouldUpdate = prefs.getBoolean("SHOULD_SAVE_DATA", true);
        if (value.equals("none") || (!shouldUpdate && !value.equals(currentDate))) {
            Log.d("SHOULDIN", value);
            editor = prefs.edit();
            editor.putString("DATE", currentDate);
            editor.putBoolean("SHOULD_SAVE_DATA", true);
            editor.commit();
        }
        authenticateSpotify();
        queue = Volley.newRequestQueue(this);
    }

    private void waitForUserInfo() {
        UserServices userService = new UserServices(queue, prefs);
        userService.get(() -> {
            User user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            Log.d("USERID", user.id);
            editor.commit();
            startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent newintent = new Intent(this, BasicActivity.class);
        startActivity(newintent);
    }

    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(SCOPES);
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    editor.apply();
                    waitForUserInfo();
                    break;
                case ERROR:
                    break;
                default:
            }
        }
    }
}