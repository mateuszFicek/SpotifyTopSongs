package com.example.spotifytopsongs.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.spotifytopsongs.Adapters.ListViewAdapter;
import com.example.spotifytopsongs.Connectors.SongService;
import com.example.spotifytopsongs.Database.DatabaseHelper;
import com.example.spotifytopsongs.Models.Song;
import com.example.spotifytopsongs.R;
import java.util.ArrayList;

/**
 * Fragment that is used to display ListView with user's top songs.
 */
public class SongFragment extends Fragment {

    private ArrayList<Song> topSongs;
    private ListView topSongsListView;
    private SongService songService;
    DatabaseHelper mDatabaseHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Cursor yesterdayData;

    public SongFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songService = new SongService(getActivity());
        mDatabaseHelper = new DatabaseHelper(getActivity());
        getTopSongs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topSongsListView = (ListView) getView().findViewById(R.id.topSongsListView);
        topSongsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song item = (Song) topSongsListView.getItemAtPosition(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getSpotifyURL()));
                startActivity(browserIntent);
            }
        });
    }

    private void updateTopSongs() {
        if (topSongs.size() > 0) {
            ListViewAdapter listViewAdapter = new ListViewAdapter(getContext(), topSongs, yesterdayData);
            topSongsListView.setAdapter(listViewAdapter);
        } else {
            Toast zeroLen = Toast.makeText(getActivity(), "Brak piosenek do wyświetlenia", Toast.LENGTH_LONG);
            zeroLen.show();
        }
    }

    private void getTopSongs() {
        songService.getTopSongsFromSpotify(() -> {
                    sharedPreferences = getContext().getSharedPreferences("SPOTIFY", 0);
                    editor = sharedPreferences.edit();
                    topSongs = songService.getTopSongs();
                    yesterdayData = mDatabaseHelper.getYesterdayData();
                    boolean shouldUpdateHistory = sharedPreferences.getBoolean("SHOULD_SAVE_DATA", true);
                    if (shouldUpdateHistory && topSongs.size() > 0) {
                        mDatabaseHelper.clearYesterday();
                        mDatabaseHelper.moveFromTodayToYesterday();
                        mDatabaseHelper.clearToday();
                        addAllSongs();
                        editor.putBoolean("SHOULD_SAVE_DATA", false);
                        editor.apply();
                    }
                    updateTopSongs();
                }
        );
    }

    public void addAllSongs() {
        for (Song song : topSongs) {
            int pos = topSongs.indexOf(song);
            AddDataToDB(pos, song.getId());
        }
    }

    public void AddDataToDB(int pos, String newEntry) {
        mDatabaseHelper.addDataToday(pos, newEntry);
    }
}
