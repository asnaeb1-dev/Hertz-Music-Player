package com.abhigyan.user.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhigyan.user.musicplayer.Databases.MyTopTracksDB;
import com.abhigyan.user.musicplayer.R;

public class PlaylistFragment extends Fragment {

    View rootView;
    TextView newPlaylistSongCount,
            newPlaylistDuration,
            topTracksSongCount,
            topTracksDuration,
            personalPlaylisySOngCount,
            peronalPlaylistDuration;

    ImageView newpLaylistIV, personalTracksIV, toptarcksIV;

    MyTopTracksDB myTopTracksDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        newPlaylistSongCount = rootView.findViewById(R.id.newPlaylstSongCount);
        newPlaylistDuration = rootView.findViewById(R.id.newPlaylistduration);
        newpLaylistIV = rootView.findViewById(R.id.newPlaylistImageView);

        topTracksSongCount = rootView.findViewById(R.id.topTracksSongCount);
        topTracksDuration = rootView.findViewById(R.id.topTracksDuration);
        toptarcksIV = rootView.findViewById(R.id.topTracksImageView);

        personalPlaylisySOngCount = rootView.findViewById(R.id.personalTracksSongCount);
        peronalPlaylistDuration = rootView.findViewById(R.id.personalTracksDuration);
        personalTracksIV = rootView.findViewById(R.id.perTracksImageView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myTopTracksDB = new MyTopTracksDB(getContext());
        Cursor c =myTopTracksDB.getAllData();
        if (c.getCount() != 0) {
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {// a loop that will iterate through all the items
                buffer.append("ALBUMNAME: " + c.getString(5) + "\n");
            }
            Log.i("Message****** ", buffer.toString());
            long countOfMyTopTracksDB = myTopTracksDB.getCount();
            newPlaylistSongCount.setText(String.valueOf(countOfMyTopTracksDB));
        }
    }
}
