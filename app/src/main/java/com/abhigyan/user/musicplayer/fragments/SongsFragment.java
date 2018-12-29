package com.abhigyan.user.musicplayer.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.abhigyan.user.musicplayer.R;
import com.abhigyan.user.musicplayer.RVAdapters.SongListRVAdapter;
import com.abhigyan.user.musicplayer.Viewpager.ViewPagerAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.FileDescriptor;
import java.util.ArrayList;

public class SongsFragment extends Fragment {

    RecyclerView songListRecyclerView;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton floatingActionButton1,
                         floatingActionButton2,
                         floatingActionButton3,
                         floatingActionButton4,
                         floatingActionButton5;

    private ArrayList<String> albummidAL = new ArrayList<>(),
                      tracknameAL = new ArrayList<>(),
                      albumNameAL = new ArrayList<>(),
                      artistNameAL = new ArrayList<>(),
                      path = new ArrayList<>(),
                      composerAL = new ArrayList<>(),
                      durationAL = new ArrayList<>(),
                      sizeAL = new ArrayList<>();

    private ArrayList<Bitmap> coverPhotoAL = new ArrayList<>();

    View rootView;
    private String[] proj1 = { "*" };
    private int called = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_songs, container, false);
        songListRecyclerView = rootView.findViewById(R.id.RVSongList);

        floatingActionMenu = rootView.findViewById(R.id.menuFAB);
        floatingActionButton5 = rootView.findViewById(R.id.menu_item5);
        floatingActionButton4 = rootView.findViewById(R.id.menu_item4);
        floatingActionButton3 = rootView.findViewById(R.id.menu_item3);
        floatingActionButton2 = rootView.findViewById(R.id.menu_item2);
        floatingActionButton1 = rootView.findViewById(R.id.menu_item1);

        floatingActionMenu.setVisibility(View.INVISIBLE);
        floatingActionMenu.animate().translationX(50f);

        floatingActionButton5.setImageDrawable(getResources().getDrawable(R.drawable.ic_crying));
        floatingActionButton5.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
        floatingActionButton4.setImageDrawable(getResources().getDrawable(R.drawable.ic_sad));
        floatingActionButton4.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
        floatingActionButton3.setImageDrawable(getResources().getDrawable(R.drawable.ic_meh));
        floatingActionButton3.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
        floatingActionButton2.setImageDrawable(getResources().getDrawable(R.drawable.ic_smile));
        floatingActionButton2.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
        floatingActionButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_happy));
        floatingActionButton1.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(called == 0) {
            askPermission();
            getSongGenre();
            called = 1;
        }

        SongListRVAdapter adapter = new SongListRVAdapter(albummidAL,path,tracknameAL, albumNameAL,artistNameAL,coverPhotoAL,sizeAL,durationAL,composerAL, getContext());
        songListRecyclerView.setAdapter(adapter);
        songListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void askPermission()
    {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

            }
        }
        else
        {
            getSongsFromExternalMemory();
        }
    }
    public void getSongsFromExternalMemory()
    {
        /**
         * This method will get all the songs from the memory of the phone
         */
        Cursor detailsCursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj1, null, null, null);
        {
            if(detailsCursor != null)
            {
                if(detailsCursor.moveToFirst())
                {
                    do{
                        int trackNameIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                        int albumdetailsIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                        int artistdetailsIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                        int pathIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                        int sizeIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
                        int composerIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER);
                        int durationIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

                        long albumId = detailsCursor.getLong(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                        Bitmap bm = null;
                        try
                        {
                            final Uri sArtworkUri = Uri
                                    .parse("content://media/external/audio/albumart");

                            Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);

                            ParcelFileDescriptor pfd = getContext().getContentResolver()
                                    .openFileDescriptor(uri, "r");

                            if (pfd != null)
                            {
                                FileDescriptor fd = pfd.getFileDescriptor();
                                bm = BitmapFactory.decodeFileDescriptor(fd);
                            }
                        } catch (Exception e) {
                            bm = ((BitmapDrawable)getResources().getDrawable(R.drawable.defaultalbumpic)).getBitmap();
                        }

                        coverPhotoAL.add(bm);
                        albummidAL.add(String.valueOf(detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));
                        tracknameAL.add(detailsCursor.getString(trackNameIndex));
                        albumNameAL.add(detailsCursor.getString(albumdetailsIndex));
                        artistNameAL.add(detailsCursor.getString(artistdetailsIndex));
                        path.add(detailsCursor.getString(pathIndex));
                        composerAL.add(detailsCursor.getString(composerIndex));
                        sizeAL.add(detailsCursor.getString(sizeIndex));
                        durationAL.add(detailsCursor.getString(durationIndex));

                    }while(detailsCursor.moveToNext());
                }
            }
        }

        detailsCursor.close();
    }
    public void getSongGenre()
    {
        String[] genreProjection = {"*"};
        Cursor genreCursor = getContext().getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,genreProjection,null,null,null);
        if(genreCursor!=null)
        {
            if(genreCursor.moveToFirst()) {
                do {
                    int genreIndex = genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                    Log.i("genreName***", genreCursor.getString(genreIndex));
                } while (genreCursor.moveToNext());
            }
        }
    }
}
