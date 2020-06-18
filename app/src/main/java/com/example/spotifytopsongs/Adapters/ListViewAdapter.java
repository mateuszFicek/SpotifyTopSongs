package com.example.spotifytopsongs.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ListView adapter used to display list of top songs.
 */
public class ListViewAdapter extends ArrayAdapter<Song> {
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
        TextView differenceView = (TextView) convertView.findViewById(R.id.difference);

        artist.setText(song.getArtist());
        title.setText(song.getName());
        positionView.setWidth(100);
        int posInt = 0;
        int diffPos = 0;
        if (!posLast[position].equals("New")) {
            posInt = Integer.parseInt(posLast[position]);
            posInt += 1;
            positionView.setText("Last - " + posInt);
        } else {
            positionView.setText("New");
        }
        positionToday.setText("Today - " + (position + 1));
        diffPos = posInt - position - 1;
        if (posLast[position].equals("New"))
            diffPos = 0;
        if (diffPos == 0) differenceView.setTextColor(Color.parseColor("#0000FF"));
        else if (diffPos > 0) differenceView.setTextColor(Color.parseColor("#00FF00"));
        else differenceView.setTextColor(Color.parseColor("#FF0000"));
        differenceView.setText(diffPos + "");
        artist.setSelected(true);
        title.setSelected(true);
        Picasso.get().load(song.getAlbumCoverURL()).into(cover);

        return convertView;
    }

    public static String[] checkLastPosition(Cursor yesterdayData, ArrayList<Song> topSongsList) {
        String[] pos = new String[20];
        int counter = 0;
        for (int i = 0; i < topSongsList.size(); i++) {
            while (yesterdayData.moveToNext()) {
                if (yesterdayData.getString(1).trim().equals(topSongsList.get(i).getId().trim())) {
                    pos[counter] = (yesterdayData.getString(0));
                }
            }
            if (pos[counter] == null) {
                pos[counter] = "New";
            }
            counter++;
            yesterdayData.moveToFirst();
        }
        return (pos);
    }
}
