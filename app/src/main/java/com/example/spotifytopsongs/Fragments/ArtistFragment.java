package com.example.spotifytopsongs.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.spotifytopsongs.Adapters.ArtistListViewAdapter;
import com.example.spotifytopsongs.Connectors.ArtistService;
import com.example.spotifytopsongs.Models.Artist;
import com.example.spotifytopsongs.R;

import java.util.ArrayList;

/**
 * Fragment used to display top artists in single ListView.
 */
public class ArtistFragment extends Fragment {
    private ArrayList<Artist> topArtists;
    private ListView topArtistListView;
    private ArtistService artistService;
    SharedPreferences sharedPreferences;

    public ArtistFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistService = new ArtistService(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("SPOTIFY", 0);
        getTopArtists();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topArtistListView = (ListView) getView().findViewById(R.id.topArtistListView);
        topArtistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist item = (Artist) topArtistListView.getItemAtPosition(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getSpotifyURL()));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    private void getTopArtists() {
        artistService.getTopArtistsFromSpotify(() -> {
                    topArtists = artistService.getTopArtists();
                    if (topArtists.size() > 0) {
                        updateTopArtists();
                    } else {
                        Toast zeroLen = Toast.makeText(getActivity(), "Brak artystów do wyświetlenia", Toast.LENGTH_LONG);
                        zeroLen.show();
                    }
                }
        );
    }

    private void updateTopArtists() {
        if (topArtists.size() > 0) {
            ArtistListViewAdapter listViewAdapter = new ArtistListViewAdapter(getContext(), topArtists);
            topArtistListView.setAdapter(listViewAdapter);
        }
    }
}
