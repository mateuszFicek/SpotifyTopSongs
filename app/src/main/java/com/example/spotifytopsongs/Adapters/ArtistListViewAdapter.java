package com.example.spotifytopsongs.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.spotifytopsongs.Models.Artist;
import com.example.spotifytopsongs.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * View adapter used to display top artists.
 */
public class ArtistListViewAdapter extends ArrayAdapter<Artist> {

    ArrayList<Artist> topArtistList;

    public ArtistListViewAdapter(Context context, ArrayList<Artist> topArtists) {
        super(context, 0, topArtists);
        topArtistList = topArtists;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Artist artist = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_artist, parent, false);
        }
        TextView artistName = convertView.findViewById(R.id.artistName);
        ImageView cover = convertView.findViewById(R.id.artistImage);
        artistName.setText(artist.getName());
        Picasso.get().load(artist.getImageURL()).into(cover);
        return convertView;
    }
}
