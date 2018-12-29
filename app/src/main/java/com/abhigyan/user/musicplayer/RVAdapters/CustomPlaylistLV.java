package com.abhigyan.user.musicplayer.RVAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhigyan.user.musicplayer.Config;
import com.abhigyan.user.musicplayer.R;

import java.util.ArrayList;

public class CustomPlaylistLV extends BaseAdapter {

    Context context;
    ArrayList<String> songNameAL = new ArrayList<>();
    ArrayList<String> durationOfSongAL = new ArrayList<>();
    ArrayList<String> dataAL = new ArrayList<>();
    LayoutInflater layoutInflater;

    public CustomPlaylistLV(Context context, ArrayList<String> songNameAL, ArrayList<String> durationOfSongAL, ArrayList<String> dataAL) {
        this.context = context;
        this.songNameAL = songNameAL;
        this.durationOfSongAL = durationOfSongAL;
        this.dataAL = dataAL;
        this.layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return songNameAL.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.playlist_content_ui,null);
        TextView songNameText = convertView.findViewById(R.id.trackNameTVPLV);
        TextView durationText = convertView.findViewById(R.id.durationTVPLV);
        ImageView menuImage = convertView.findViewById(R.id.menuPLV);

        Config config = new Config();
        songNameText.setText(config.parseTrackName(songNameAL.get(position)));
        durationText.setText(config.parseTime(durationOfSongAL.get(position)));

        return convertView;
    }
}
