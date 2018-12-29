package com.abhigyan.user.musicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhigyan.user.musicplayer.RVAdapters.ArtistRVAdater;
import com.abhigyan.user.musicplayer.R;

import java.util.ArrayList;

public class ArtistFragment extends Fragment {

    String[] projection = { MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM };
    View myView;

    ArrayList<String> artistNameARVList = new ArrayList<>(),
                      albumNameARVList = new ArrayList<>();

    RecyclerView artistRecyclerView;

    int called  = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_artist, container, false);
        artistRecyclerView = myView.findViewById(R.id.artistARV);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        if(called  == 0) {
            getSongBasedOnArtist();
            called  = 1;
        }
        ArtistRVAdater adapter = new ArtistRVAdater(artistNameARVList, albumNameARVList,getContext());
        artistRecyclerView.setAdapter(adapter);
        artistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    public void getSongBasedOnArtist()
    {
        Cursor detailsCursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        {
            if(detailsCursor != null)
            {
                if(detailsCursor.moveToFirst())
                {
                    do{
                        int indexAlbumName = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                        int artistIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);

                        albumNameARVList.add(detailsCursor.getString(indexAlbumName));
                        artistNameARVList.add(detailsCursor.getString(artistIndex));
                        Log.i("artistNames*******", detailsCursor.getString(artistIndex));
                    }while(detailsCursor.moveToNext());
                }
            }
        }
        detailsCursor.close();
    }
}
