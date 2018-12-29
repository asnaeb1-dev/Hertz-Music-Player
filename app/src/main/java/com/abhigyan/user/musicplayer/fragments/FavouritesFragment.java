package com.abhigyan.user.musicplayer.fragments;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhigyan.user.musicplayer.Config;
import com.abhigyan.user.musicplayer.Databases.FavouritesDB;
import com.abhigyan.user.musicplayer.RVAdapters.FavouritesRV;
import com.abhigyan.user.musicplayer.R;
import com.abhigyan.user.musicplayer.RVAdapters.SongListRVAdapter;

import java.io.FileDescriptor;
import java.util.ArrayList;

public class FavouritesFragment extends Fragment {

    View favouritesView;
    RecyclerView favouritesRecyclerView;

    ArrayList<String>   trackNameF_AL = new ArrayList<>(),
                        pathF_AL = new ArrayList<>(),
                        albumNameF_AL = new ArrayList<>(),
                        artistNameF_AL = new ArrayList<>();

    ArrayList<Bitmap> coverPhotosF_AL = new ArrayList<>();

    int called  = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        favouritesView = getLayoutInflater().inflate(R.layout.fragment_favourites,container,false);
        favouritesRecyclerView = favouritesView.findViewById(R.id.recyclerViewFavsList);

        return favouritesView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(called == 0) {
            FavouritesDB favouritesDB = new FavouritesDB(getContext());
            Config config = new Config();
            Cursor cur = favouritesDB.getAllData();
            while (cur.moveToNext()) {

                trackNameF_AL.add(cur.getString(2));
                pathF_AL.add(cur.getString(3));
                albumNameF_AL.add(cur.getString(4));
                artistNameF_AL.add(cur.getString(5));

                coverPhotosF_AL.add(config.getPics(getContext(), Long.parseLong(cur.getString(1))));
            }
            called  =1;
        }

        FavouritesRV adapter = new FavouritesRV(getContext(),trackNameF_AL,albumNameF_AL,artistNameF_AL,coverPhotosF_AL);
        favouritesRecyclerView.setAdapter(adapter);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

}
