package com.example.spotifytopsongs.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spotifytopsongs.Models.Song;
import com.example.spotifytopsongs.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListViewAdapter extends ArrayAdapter<Song> {
    Context context;
    ArrayList<Song> topSongsList;

    public ListViewAdapter(Context context, ArrayList<Song> topSongs) {
        super(context, 0, topSongs);
        topSongsList = topSongs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song song = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }

        TextView artist = (TextView) convertView.findViewById(R.id.artistTextView);
        TextView title = (TextView) convertView.findViewById(R.id.titleTextView);
        ImageView cover = (ImageView) convertView.findViewById(R.id.coverImage);

        artist.setText(song.getArtist());
        title.setText(song.getName());

        artist.setSelected(true);
        title.setSelected(true);
        Picasso.get().load(song.getAlbumCoverURL()).into(cover);

        return convertView;
    }
}
