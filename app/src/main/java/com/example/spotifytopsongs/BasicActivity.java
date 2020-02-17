package com.example.spotifytopsongs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spotifytopsongs.Adapters.ListViewAdapter;
import com.example.spotifytopsongs.Connectors.SongService;
import com.example.spotifytopsongs.Models.Song;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main activity which shows all the data that was received from Spotify API.
 */
public class BasicActivity extends AppCompatActivity {


    private TextView userView;
    private TextView songView;
    private TextView currentSongView;
    private ListView topSongsListView;
    private Button addBtn;
    private Song currentSong;
    private SongService songService;
    private ArrayList<Song> recentlyPlayedTracks;
    private ArrayList<Song> topSongs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        songService = new SongService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);
        addBtn = (Button) findViewById(R.id.add);
        currentSongView = (TextView) findViewById(R.id.currentSong);
        topSongsListView = (ListView) findViewById(R.id.topSongsListView);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));
        addBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            }
        });
        getTracks();
        getCurrentSong();
        getTopSongs();
    }


    private void getTracks() {
        songService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = songService.getSongs();
            updateRecentSongs();
        });
    }

    private void updateRecentSongs() {
        if (recentlyPlayedTracks.size() > 0) {
            songView.setText(recentlyPlayedTracks.get(0).getName());
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
            currentSongView.setText(currentSong.getName());
        }
    }

    private void getTopSongs() {
        songService.getTopSongsFromSpotify(() -> {
                    topSongs = songService.getTopSongs();
                    updateTopSongs();
                }
        );
    }

    private void updateTopSongs() {
        if(topSongs.size() > 0){
            ListViewAdapter listViewAdapter = new ListViewAdapter(this,topSongs);
            topSongsListView.setAdapter(listViewAdapter);
        }
    }

}
