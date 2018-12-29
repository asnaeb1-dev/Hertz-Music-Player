package com.abhigyan.user.musicplayer.Activities;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhigyan.user.musicplayer.R;
import com.abhigyan.user.musicplayer.RVAdapters.AlbumContentsRV;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Random;

public class AlbumContentsActivity extends AppCompatActivity {

    String nameOfAlbum, albumID;
    String projection[] = { "*" };
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> durationList = new ArrayList<>();
    ArrayList<String> artistName = new ArrayList<>();

    ImageView imageView;
    TextView songSizeTV;
    RecyclerView recyclerViewAC;

    CollapsingToolbarLayout albumContentsCToolBar;

    int looperEnabled = 0;
    Random rand;
    int posOfPointerOnList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_contents);

        Intent intent = getIntent();
        nameOfAlbum  = intent.getStringExtra("nameOfAlbum");
        albumID = intent.getStringExtra("albumID");

        imageView = findViewById(R.id.albumImageAlbumContent);
        songSizeTV = findViewById(R.id.songSize);
        recyclerViewAC = findViewById(R.id.RVAlbumContents);
        albumContentsCToolBar = findViewById(R.id.collapsingToolbarAC);
        albumContentsCToolBar.setTitle(nameOfAlbum);

        getSongOfAlbum("'"+nameOfAlbum+"'");

        Bitmap bm = getImg(albumID);
        if(bm!=null) {
            imageView.setImageBitmap(bm);
        }
        else
        {
            imageView.setImageResource(R.drawable.defaultalbumpic);
        }

        AlbumContentsRV adapter = new AlbumContentsRV(nameList, durationList,artistName,nameOfAlbum, this);
        recyclerViewAC.setAdapter(adapter);
        recyclerViewAC.setLayoutManager(new LinearLayoutManager(this));

        rand = new Random();
        posOfPointerOnList = rand.nextInt(nameList.size());

        if(nameList.size()>1) {
            songSizeTV.setText(nameList.size() + " songs");
        }
        else {
            songSizeTV.setText(nameList.size() + " song");
        }
    }

    public void getSongOfAlbum(String albumName)
    {
        Cursor detailsCursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,MediaStore.Audio.Media.ALBUM+" = "+albumName,null,null);
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

                        int artistIndex = detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);

                        nameList.add(detailsCursor.getString(displayname));
                        durationList.add(detailsCursor.getString(duration));
                        artistName.add(detailsCursor.getString(artistIndex));

                    }while(detailsCursor.moveToNext());

                }
            }
        }
        detailsCursor.close();
    }

    public void looperFunction(View view)
    {
            looperEnabled = 1;
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("looper", looperEnabled);
            intent.putExtra("callSource", 2);
            intent.putExtra("positionpointer",posOfPointerOnList);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    public Bitmap getImg(String albumID)
    {
        Bitmap bm = null;
        try
        {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(albumID));

            ParcelFileDescriptor pfd = this.getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null)
            {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("abhigyans error", e.getMessage());

        }

        return bm;
    }
}
