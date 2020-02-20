package com.example.spotifytopsongs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spotifytopsongs.Adapters.ListViewAdapter;
import com.example.spotifytopsongs.Connectors.SongService;
import com.example.spotifytopsongs.Models.Playlist;
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
    private Button createPlaylistButton;
    private Playlist topPlaylist;
    private int Counter = 0;
    private AddSongsOnCallback addSongsOnCallback;

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
        createPlaylistButton = (Button) findViewById(R.id.createPlaylistButton);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        String wasPlaylistMade = sharedPreferences.getString("wasPlaylistMade", "");
        userView.setText(sharedPreferences.getString("userid", "No User"));
        addBtn.setOnClickListener(new View.OnClickListener() {
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
                    Log.d("COUNTER", Integer.toString(Counter));
                } else {
                    Counter++;
                    Log.d("COUNTER", Integer.toString(Counter));
                }
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
                    updateTopSongs();
                }
        );
    }

    private void updateTopSongs() {
        if (topSongs.size() > 0) {
            ListViewAdapter listViewAdapter = new ListViewAdapter(this, topSongs);
            topSongsListView.setAdapter(listViewAdapter);
        }
    }

    private void createPlaylist() {
        Log.d("Gdzie?", "Tu jestem");
        addSongsOnCallback = new AddSongsOnCallback(getApplicationContext(), topSongs);

    }

    private void addSongsTopPlaylist() {

    }

}
