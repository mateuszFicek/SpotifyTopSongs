package com.example.spotifytopsongs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.spotifytopsongs.Adapters.SpinnerAdapter;
import com.example.spotifytopsongs.Connectors.ArtistService;
import com.example.spotifytopsongs.Connectors.PlaylistService;
import com.example.spotifytopsongs.Connectors.SongService;
import com.example.spotifytopsongs.Connectors.UserServices;
import com.example.spotifytopsongs.Database.DatabaseHelper;
import com.example.spotifytopsongs.Models.Artist;
import com.example.spotifytopsongs.Models.Playlist;
import com.example.spotifytopsongs.Models.Song;
import com.example.spotifytopsongs.Models.User;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main activity which shows all the data that was received from Spotify API.
 */
public class BasicActivity extends AppCompatActivity {


    private TextView userView;
    private TextView songView;
    private TextView currentSongView;
    private Button refresh;
    private Button addSongButton;
    private Button topButton;
    private ImageView userImage;

    private UserServices userServices;
    private Song currentSong;
    private SongService songService;
    private PlaylistService playlistService;
    private ArrayList<Song> recentlyPlayedTracks;
    private ArrayList<Song> topSongs;
    private Button createPlaylistButton;
    private ArrayList<Playlist> playlists;
    private int Counter = 0;
    private AddSongsOnCallback addSongsOnCallback;
    private Spinner spinner;
    private SpinnerAdapter spinnerAdapter;
    private Playlist currentPlaylist;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RequestQueue queue;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        queue = Volley.newRequestQueue(this);
        sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userServices = new UserServices(queue, sharedPreferences);
        songService = new SongService(getApplicationContext());
        playlistService = new PlaylistService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);
        refresh = (Button) findViewById(R.id.refresh);
        addSongButton = (Button) findViewById(R.id.addSong);
        topButton = (Button) findViewById(R.id.topButton);
        currentSongView = (TextView) findViewById(R.id.currentSong);
        createPlaylistButton = (Button) findViewById(R.id.createPlaylistButton);
        spinner = (Spinner) findViewById(R.id.playlistSpinner);
        userImage = (ImageView) findViewById(R.id.userImage);
        editor = sharedPreferences.edit();
        userView.setText(sharedPreferences.getString("userid", "No User"));
        getTopSongs();
        getPlaylists();
        getTracks();
        getCurrentSong();
        getUser();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        createPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Counter == 0) {
                    createPlaylist();
                    Counter++;
                } else {
                    Counter++;
                }
            }
        });

        addSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlaylist != null && currentSong != null) {
                    playlistService.checkIfSongIsOnPlaylist(getApplicationContext(), currentPlaylist, currentSong);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Playlist playlist = (Playlist) parent.getItemAtPosition(position);
                currentPlaylist = playlist;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TopActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUser() {
        userServices.get(() -> {
            user = userServices.getUser();
            if(!user.getImageURL().equals(""))
                new LoadImage(userImage).execute(user.getImageURL());
        });
    }

    private void getTracks() {
        songService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = songService.getSongs();
            updateRecentSongs();
        });
    }

    private void updateRecentSongs() {
        if (recentlyPlayedTracks.size() > 0) {
            String recent = recentlyPlayedTracks.get(0).getName() + " - " + recentlyPlayedTracks.get(0).getArtist();
            songView.setText(recent);
        }
    }

    private void getCurrentSong() {
        songService.getCurrentSong(() -> {
                    currentSong = songService.getSong();
                    updateCurrentSong();
                }
        );
    }

    private void updateCurrentSong() {
        if (currentSong != null) {
            String currentSongTitle = currentSong.getName() + " - " + currentSong.getArtist();
            currentSongView.setText(currentSongTitle);
        }
    }

    private void getTopSongs() {
        songService.getTopSongsFromSpotify(() -> {
                    topSongs = songService.getTopSongs();
                }
        );
    }

    private void createPlaylist() {
        addSongsOnCallback = new AddSongsOnCallback(getApplicationContext(), topSongs);
    }

    private void getPlaylists() {
        playlistService.getUserPlaylists(() -> {
            playlists = playlistService.getPlay();
            updateSpinner();
        });
    }

    private void updateSpinner() {
        if (playlists.size() > 0) {
            spinnerAdapter = new SpinnerAdapter(this, playlists);
            spinner.setAdapter(spinnerAdapter);
        }

    }

    private BroadcastReceiver wifiState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    topButton.setEnabled(true);
                    createPlaylistButton.setEnabled(true);
                    addSongButton.setEnabled(true);
                    Toast toastEn = Toast.makeText(getApplicationContext(), "Wifi is enabled. Buttons enabled.", Toast.LENGTH_LONG);
                    toastEn.show();
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    topButton.setEnabled(false);
                    createPlaylistButton.setEnabled(false);
                    addSongButton.setEnabled(false);
                    Toast toastDis = Toast.makeText(getApplicationContext(), "Wifi is disabled. Buttons disabled.", Toast.LENGTH_LONG);
                    toastDis.show();
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiState, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiState);
    }
}
