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

//    ListViewAdapter(Context context, Song songs[],String songNames[]){
//        super(context, R.layout.row, R.id.titleTextView, songNames);
//        this.context = context;
//        this.topSongsList = songs;
//        this.songNames = songNames;
//    }
//
//    public ArrayList<String> getSongNames(ArrayList<Song> songs){
//        ArrayList names = new ArrayList<String>();
//        ArrayList urls = new ArrayList<String>();
//        ArrayList artists = new ArrayList<String>();
//
//        String name,artist,url;
//        for (int i = 0; i < songs.size(); i++){
//            name = songs.get(i).getName();
//            url = songs.get(i).getAlbumCoverURL();
//            names.add(name);
//            urls.add(url);
//        }
//        this.songNames = names;
//    }

    public ListViewAdapter(Context context, ArrayList<Song> topSongs){
        super(context,0,topSongs);
        topSongsList = topSongs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song song = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }

        TextView artist = (TextView) convertView.findViewById(R.id.artistTextView);
        TextView title = (TextView) convertView.findViewById(R.id.titleTextView);
        ImageView cover = (ImageView) convertView.findViewById(R.id.coverImage);

        artist.setText(song.getArtist());
        title.setText(song.getName());
        Picasso.get().load(song.getAlbumCoverURL()).into(cover);

        return convertView;
    }
}
