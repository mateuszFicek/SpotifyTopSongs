package com.example.spotifytopsongs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.spotifytopsongs.Models.Playlist;
import com.example.spotifytopsongs.R;
import java.util.ArrayList;

/**
 * Spinner adapter used to display all playlists user has.
 * It's the dropdown menu that user can chose playlist from.
 */
public class SpinnerAdapter extends ArrayAdapter {
    ArrayList<Playlist> playlistsAll;
    public SpinnerAdapter(Context context, ArrayList<Playlist> playlists) {
        super(context, 0, playlists);
        this.playlistsAll = playlists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_row, parent, false
            );
        }
        TextView textView = convertView.findViewById(R.id.playlistNameTextView);
        Playlist playlist = (Playlist) getItem(position);
        if (playlist != null){
            textView.setText(playlist.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_row, parent, false
            );
        }
        TextView textView = convertView.findViewById(R.id.playlistNameTextView);
        Playlist playlist = (Playlist) getItem(position);
        if (playlist != null){
            textView.setText(playlist.getName());
        }
        return convertView;
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_row, parent, false
            );
        }
        TextView textView = convertView.findViewById(R.id.playlistNameTextView);
        Playlist playlist = (Playlist) getItem(position);
        if (playlist != null){
            textView.setText(playlist.getName());
        }
        return convertView;
    }
}
