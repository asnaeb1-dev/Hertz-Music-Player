package com.abhigyan.user.musicplayer.fragments;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.abhigyan.user.musicplayer.Activities.AlbumContentsActivity;
import com.abhigyan.user.musicplayer.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AlbumFragment extends Fragment {

    View rootView;
    String projection[] = {MediaStore.Audio.Media.ALBUM_ID,MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST};

    GridLayout gridLayoutAlbum;

    ArrayList<String> arrayListAlbumName = new ArrayList<>(),
                    arrayListAlbumID = new ArrayList<>(),
                    hashSetArtist = new ArrayList<>();

    ArrayList<Bitmap> picArrayList = new ArrayList<>();
    int called = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_album, container, false);
        gridLayoutAlbum = rootView.findViewById(R.id.albumGridLayout);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(called == 0) {
            getAllSongs();
            called  = 1;
        }
        dealWithLayout();

    }

          public void getAllSongs() {
            /**
             * This method will get all the songs from the memory of the phone
             */
            Cursor detailsCursor = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
            {
                if(detailsCursor != null)
                {
                    if(detailsCursor.moveToFirst())
                    {
                        do{
                            int albumIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                            int artistIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                            int indexalbumid = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);


                            Bitmap bm = null;
                            try
                            {
                                final Uri sArtworkUri = Uri
                                        .parse("content://media/external/audio/albumart");

                                Uri uri = ContentUris.withAppendedId(sArtworkUri, detailsCursor.getLong(indexalbumid));

                                ParcelFileDescriptor pfd = getContext().getContentResolver().openFileDescriptor(uri, "r");

                                if (pfd != null)
                                {
                                    FileDescriptor fd = pfd.getFileDescriptor();
                                    bm = BitmapFactory.decodeFileDescriptor(fd);
                                }
                            }
                            catch (FileNotFoundException e)
                            {
                                bm = BitmapFactory.decodeResource(getResources(),R.drawable.defaultalbumpic);
                            }


                            arrayListAlbumName.add(detailsCursor.getString(albumIndex));
                            arrayListAlbumID.add(detailsCursor.getString(indexalbumid));
                            picArrayList.add(bm);
                        }
                        while(detailsCursor.moveToNext());
                    }
                }
            }
            detailsCursor.close();
        }

        public void dealWithLayout()
        {
            for(int i = 0;i<picArrayList.size();i++) {
                populateLayout(picArrayList.get(i), arrayListAlbumName.get(i), arrayListAlbumID.get(i));
            }
        }

        public void populateLayout(Bitmap bitmap, final String albumNm, final String albumID )
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(width/2,height/3));
            imageView.setPadding(2,5,2,5);
            imageView.setImageBitmap(bitmap);
            if(Build.VERSION.SDK_INT>=21) {
                imageView.setElevation(10f);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            gridLayoutAlbum.addView(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), AlbumContentsActivity.class);
                    intent.putExtra("nameOfAlbum", albumNm);
                    intent.putExtra("albumID", albumID);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            });
        }

}
