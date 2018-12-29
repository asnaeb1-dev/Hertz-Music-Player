package com.abhigyan.user.musicplayer.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.abhigyan.user.musicplayer.R;
import com.abhigyan.user.musicplayer.RVAdapters.AlbumContentsRV;
import com.abhigyan.user.musicplayer.RVAdapters.ArtistContentRV;

import java.util.ArrayList;

public class ArtistContentActivity extends AppCompatActivity {

    private ArrayList<String> trackNameArrayList = new ArrayList<>(),
                                durationArrayList = new ArrayList<>(),
                                albumNameArrayList = new ArrayList<>();

    private TextView songSizeTV;

    String artistName;
    private RecyclerView recyclerView;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_content);

        Intent intent = getIntent();
        intent.getStringExtra("artistName");

        artistName = "'"+intent.getStringExtra("artistName")+"'";
        getArtistDatabase(artistName);

        recyclerView = findViewById(R.id.RVArtistContent);
        collapsingToolbarLayout = findViewById(R.id.artistCTL);
        songSizeTV = findViewById(R.id.songSizeArTV);
        collapsingToolbarLayout.setTitle(artistName);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        ArtistContentRV adapter = new ArtistContentRV(trackNameArrayList, durationArrayList,albumNameArrayList,artistName, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(albumNameArrayList.size()>1) {
            songSizeTV.setText(albumNameArrayList.size() + " songs");
        }
        else {
            songSizeTV.setText(albumNameArrayList.size() + " song");
        }
    }

    private void getArtistDatabase(String artistName) {

        String[] projection ={"*"};

        Cursor detailsCursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,MediaStore.Audio.Media.ARTIST+" = "+artistName,null,null);
        {
            if(detailsCursor!=null)
            {
                if(detailsCursor.moveToFirst())
                {
                    do{
                        //int albumIndex = detailscursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                        int displayname = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                        Log.i("loggin*********", detailsCursor.getString(displayname));
                        int duration = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

                        int albumIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);

                        trackNameArrayList.add(detailsCursor.getString(displayname));
                        durationArrayList.add(detailsCursor.getString(duration));
                        albumNameArrayList.add(detailsCursor.getString(albumIndex));

                    }while(detailsCursor.moveToNext());
                }
            }
        }
        detailsCursor.close();
    }
}
