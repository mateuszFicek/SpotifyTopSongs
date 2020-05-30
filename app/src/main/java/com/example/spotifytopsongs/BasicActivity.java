package com.example.spotifytopsongs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.spotifytopsongs.Adapters.ListViewAdapter;
import com.example.spotifytopsongs.Adapters.SpinnerAdapter;
import com.example.spotifytopsongs.Connectors.ArtistService;
import com.example.spotifytopsongs.Connectors.PlaylistService;
import com.example.spotifytopsongs.Connectors.SongService;
import com.example.spotifytopsongs.Database.DatabaseHelper;
import com.example.spotifytopsongs.Models.Artist;
import com.example.spotifytopsongs.Models.Playlist;
import com.example.spotifytopsongs.Models.Song;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main activity which shows all the data that was received from Spotify API.
 */
public class BasicActivity extends AppCompatActivity {


    private TextView userView;
    private TextView songView;
    private TextView currentSongView;
    private ListView topSongsListView;
    private Button refresh;
    private Button addSongButton;
    private Song currentSong;
    private SongService songService;
    private PlaylistService playlistService;
    private ArtistService artistService;
    private ArrayList<Song> recentlyPlayedTracks;
    private ArrayList<Song> topSongs;
    private ArrayList<Artist> topArtists;
    private Button createPlaylistButton;
    private ArrayList<Playlist> playlists;
    private int Counter = 0;
    private AddSongsOnCallback addSongsOnCallback;
    private Spinner spinner;
    private SpinnerAdapter spinnerAdapter;
    private Playlist currentPlaylist;
    private HashMap<Integer, Integer> differenceSinceLastUpdate;
    DatabaseHelper mDatabaseHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        songService = new SongService(getApplicationContext());
        playlistService = new PlaylistService(getApplicationContext());
        artistService = new ArtistService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);
        refresh = (Button) findViewById(R.id.refresh);
        addSongButton = (Button) findViewById(R.id.addSong);

        currentSongView = (TextView) findViewById(R.id.currentSong);
        topSongsListView = (ListView) findViewById(R.id.topSongsListView);
        createPlaylistButton = (Button) findViewById(R.id.createPlaylistButton);
        spinner = (Spinner) findViewById(R.id.playlistSpinner);

        sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        editor = sharedPreferences.edit();
        userView.setText(sharedPreferences.getString("userid", "No User"));
        differenceSinceLastUpdate = new HashMap<>();
        getTopSongs();
        getPlaylists();
        getTracks();
        getCurrentSong();
        getTopArtists();
        mDatabaseHelper = new DatabaseHelper(this);

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
        topSongsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song item = (Song) topSongsListView.getItemAtPosition(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getSpotifyURL()));
                startActivity(browserIntent);
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
    }

    public void addAllSongs() {
        for (Song song : topSongs) {
            AddDataToDB(song.getId());
        }
    }

    public void AddDataToDB(String newEntry) {
        boolean insertData = mDatabaseHelper.addDataToday(newEntry);
        if (insertData) {
            Log.d(newEntry, "UDALO SIE");
        } else {
            Log.d(newEntry, "Sth WENT WRONG");
        }
    }

    public void getDataFromDatabase() {
        Cursor dataFromYesterday = mDatabaseHelper.getYesterdayData();
        for (Song song : topSongs) {
            int currentPosition = topSongs.indexOf(song);
            int lastPosition = 99;
            while (dataFromYesterday.moveToNext()) {
                Log.d("ZERO COL : ", dataFromYesterday.getString(0));
                Log.d("FIRST COL: ", dataFromYesterday.getString(1));
                if (dataFromYesterday.getString(1) == song.getId()) {
                    lastPosition = dataFromYesterday.getPosition();
                    break;
                }
            }

            if(lastPosition == 99)
                differenceSinceLastUpdate.put(currentPosition,lastPosition);
            else{
                differenceSinceLastUpdate.put(currentPosition, lastPosition-currentPosition);
            }
        }
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
                    boolean shouldUpdateHistory = sharedPreferences.getBoolean("SHOULD_SAVE_DATA", false);
                    Log.d("SHOULD UPDATE", shouldUpdateHistory + "");
                    if (shouldUpdateHistory) {
                        mDatabaseHelper.clearYesterday();
                        mDatabaseHelper.moveFromTodayToYesterday();
                        mDatabaseHelper.clearToday();
                        addAllSongs();
                        editor.putBoolean("SHOULD_SAVE_DATA", false);
                        editor.commit();
                    }
                    getDataFromDatabase();
                }
        );
    }

    private void getTopArtists() {
        artistService.getTopArtistsFromSpotify(() -> {
                    topArtists = artistService.getTopArtists();
//                    updateTopArtists();
                }
        );
    }

    private void updateTopSongs() {
        if (topSongs.size() > 0) {
            ListViewAdapter listViewAdapter = new ListViewAdapter(this, topSongs, differenceSinceLastUpdate);
            topSongsListView.setAdapter(listViewAdapter);
        }
    }

    private void updateTopArtists() {
        if (topArtists.size() > 0) {
            ListViewAdapter listViewAdapter = new ListViewAdapter(this, topSongs, differenceSinceLastUpdate);
            topSongsListView.setAdapter(listViewAdapter);
        }
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


}
