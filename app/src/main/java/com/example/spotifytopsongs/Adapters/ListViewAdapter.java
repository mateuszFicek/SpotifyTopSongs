package com.example.spotifytopsongs.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListViewAdapter extends ArrayAdapter<Song> {
    Context context;
    ArrayList<Song> topSongsList;
    Cursor yesterdayData;
    String[] posLast;

    public ListViewAdapter(Context context, ArrayList<Song> topSongs, Cursor data) {
        super(context, 0, topSongs);
        topSongsList = topSongs;
        yesterdayData = data;
        posLast = checkLastPosition(yesterdayData, topSongsList);
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
        TextView positionView = (TextView) convertView.findViewById(R.id.positionYesterday);
        TextView positionToday = (TextView) convertView.findViewById(R.id.positionToday);
        artist.setText(song.getArtist());
        title.setText(song.getName());
        positionView.setWidth(100);
        if(!posLast[position].equals("New")){
            int posInt = Integer.parseInt(posLast[position]);
            posInt +=1;
            positionView.setText("Last - " + posInt);
        } else{
            positionView.setText("New");
        }
        positionToday.setText("Today - " + (position + 1));
        artist.setSelected(true);
        title.setSelected(true);
        Picasso.get().load(song.getAlbumCoverURL()).into(cover);

        return convertView;
    }

    public static String[] checkLastPosition(Cursor yesterdayData, ArrayList<Song> topSongsList) {
        String[] pos = new String[20];
        int counter = 0;
        while (yesterdayData.moveToNext()) {
            for (int i = 0; i < topSongsList.size(); i++) {
                if (yesterdayData.getString(1).trim().equals(topSongsList.get(i).getId().trim())) {
                    Log.d("MATCH", yesterdayData.getString(0));
                    pos[counter] = (yesterdayData.getString(0));
                    Log.d("POS", pos[counter]);
                    break;
                }
            }
            if(pos[counter] == null){
                pos[counter] = "New";
            }
            counter++;
        }

        return (pos);
    }
}
