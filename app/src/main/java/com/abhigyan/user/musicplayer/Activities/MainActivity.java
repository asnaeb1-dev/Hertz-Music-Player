package com.abhigyan.user.musicplayer.Activities;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhigyan.user.musicplayer.Config;
import com.abhigyan.user.musicplayer.Databases.FavouritesDB;
import com.abhigyan.user.musicplayer.TheGodClass;
import com.abhigyan.user.musicplayer.Viewpager.DepthTransformation;
import com.abhigyan.user.musicplayer.R;
import com.abhigyan.user.musicplayer.RVAdapters.CustomPlaylistLV;
import com.abhigyan.user.musicplayer.Viewpager.ViewPagerAdapter;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * THIS ACTIVITY DEALS WITH THE PLAY WINDOW. HERE USER CAN PLAY ANY MUSIC FROM STORAGE.
 */

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    CoordinatorLayout coordinatorLayoutSongPlay;
    ImageView favouriteButton, shuffleButton, loopButton, playListButton;
    FloatingActionButton playpauseButton;
    ImageButton nextButton, PrevButton;
    SeekBar songSeekbar;
    TextView endTimeText1, startTime1;
    CountDownTimer countDownTimer;
    Toolbar toolbar1;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ViewPager viewPager;

    Cursor detailscursor;

    MediaPlayer mediaPlayer;

    private Boolean countDownEnabled = false,
                    songIsPlaying = false,
                    shuffleEnabled = false,
                    addedToFavourites = false;

    int posOfPointerOnList;

    GestureDetectorCompat gestureDetector;

    Random rand = new Random();
    Config config = new Config();
    FavouritesDB favouritesDB;

    private ArrayList<String> trackNameArrayList = new ArrayList<>(),
                                albumNameArrayList = new ArrayList<>(),
                                pathArrayList = new ArrayList<>(),
                                sizeArrayList = new ArrayList<>(),
                                durationArrayList = new ArrayList<>(),
                                artistNameArrayList = new ArrayList<>(),
                                composerArrayList = new ArrayList<>();

    private ArrayList<Bitmap> coverPhotoArrayList = new ArrayList<>();
    private ArrayList<Long> albumIDArrayList = new ArrayList<>();

    private String albumName, artistName;
    private int callsc;

    int timeX = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.play_screen_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_Option);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search songs...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.dailogxml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.favOption:
                break;

            case R.id.sleepTimer:
                //engage sleep Timer
                sleepTimeFunction();
                break;

            case R.id.detailsOption:
                //get all songs from the artist

                Config config = new Config();
                ArrayList<String> detailsarrayList = new ArrayList<>();
                detailsarrayList.add("Track- "+trackNameArrayList.get(posOfPointerOnList));
                detailsarrayList.add("Album- "+albumNameArrayList.get(posOfPointerOnList));
                detailsarrayList.add("Artist- "+artistNameArrayList.get(posOfPointerOnList));
                detailsarrayList.add("Composer- "+composerArrayList.get(posOfPointerOnList));
                detailsarrayList.add("Path- "+pathArrayList.get(posOfPointerOnList));
                if(callsc != 4) {
                    detailsarrayList.add("Duration- " + config.parseTime(durationArrayList.get(posOfPointerOnList)));
                    detailsarrayList.add("Size- "+config.parseSize(sizeArrayList.get(posOfPointerOnList)));
                }
                else
                {
                    detailsarrayList.add("Duration- " + durationArrayList.get(posOfPointerOnList));
                    detailsarrayList.add("Size- "+sizeArrayList.get(posOfPointerOnList));
                }


                View detailsViewDailog = this.getLayoutInflater().inflate(R.layout.details_dailog_ui,null);
                android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(this);
                ListView listView = detailsViewDailog.findViewById(R.id.detailsLV);
                ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,detailsarrayList);
                listView.setAdapter(arrayAdapter);

                mBuilder.setView(detailsViewDailog);
                mBuilder.show();

                break;

            case R.id.shareOption:
                //share song with friends
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent prevIntent = getIntent();
        posOfPointerOnList = prevIntent.getIntExtra("positionpointer",0);//imp
        callsc = prevIntent.getIntExtra("callSource",0);

        findAllUI();

        if(callsc == 1)
        {
            getAllSongDetails(callsc);
        }
        else if(callsc == 2)
        {
            albumName = "'"+prevIntent.getStringExtra("albumname")+"'";
            if(prevIntent.getIntExtra("looper",0) == 1)
            {
                shuffleEnabled = true;
                EnableDisableShuffle();
            }
            getAllSongDetails(callsc);
        }
        else if(callsc == 3)
        {
            //calling from artist fragment
            artistName = prevIntent.getStringExtra("artistName");
            getAllSongDetails(callsc);

        }
        else if(callsc == 4)
        {
            //calling from favourites
            favouriteButton.setEnabled(false);
            favouriteButton.setAlpha(0.5f);
            getFavouritesFromDatabase();
        }

        //#################################VIEW PAGER####################################
        ViewPagerAdapter viewPagerAdapter =  new ViewPagerAdapter(MainActivity.this, coverPhotoArrayList);
        DepthTransformation depthTransformation = new DepthTransformation();
        viewPager.setPageTransformer(true,depthTransformation);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(posOfPointerOnList, true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                posOfPointerOnList = position-1;
                playNextViewPager();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //###############################################################################

        //###############################GESTURE DETECTOR#########################################
        gestureDetector = new GestureDetectorCompat(MainActivity.this,MainActivity.this);

        //########################################################################################

        favouritesDB = new FavouritesDB(this);
        mediaPlayer = new MediaPlayer();
        activateMediaPlayer(pathArrayList.get(posOfPointerOnList));
        collapsingToolbarLayout.setTitle(trackNameArrayList.get(posOfPointerOnList));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.cardview_dark_background));

        if(callsc!=4) {
            detailscursor.close();
        }
        activateSeekBarSeeking();

    }

   /* public void seekBarFunction()
    {
        songSeekbar.setMax(mediaPlayer.getDuration());
        songSeekbar.setProgress(0);
        final Handler handler = new Handler();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                songSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        });
    }*/

//##################GET DETAILS#####################
    public void getAllSongDetails(int callSource)
    {
        String[] projection = {"*"};
        String[] genreProj ={"*"};

        if(callSource == 1)
        {
            //calling from songList
            detailscursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection, null, null,null);
        }
        else if(callSource == 2)
        {
            //calling from albumlist
            detailscursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection, MediaStore.Audio.Media.ALBUM+" = "+albumName, null,null);
        }
        else if(callSource == 3)
        {
            //calling from artist list
            detailscursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection, MediaStore.Audio.Media.ARTIST+" = "+artistName, null,null);
        }
        else if( callSource == 4)
        {
            //calling from playlist
        }

        if(detailscursor !=null)
        {
            if(detailscursor.moveToFirst())
            {
                do {
                    int albumIDIndex = detailscursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
                    int albumNameIndex = detailscursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                    int trackNameIndex = detailscursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    int artistNameIndex = detailscursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                    int pathIndex = detailscursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    int sizeIndex = detailscursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
                    int composerIndex = detailscursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER);
                    int durationIndex = detailscursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

                    Bitmap bm = null;
                    try
                    {
                        final Uri sArtworkUri = Uri
                                .parse("content://media/external/audio/albumart");

                        Uri uri = ContentUris.withAppendedId(sArtworkUri, detailscursor.getLong(albumIDIndex));

                        ParcelFileDescriptor pfd = this.getContentResolver()
                                .openFileDescriptor(uri, "r");

                        if (pfd != null)
                        {
                            FileDescriptor fd = pfd.getFileDescriptor();
                            bm = BitmapFactory.decodeFileDescriptor(fd);
                            coverPhotoArrayList.add(bm);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        bm = BitmapFactory.decodeResource(getResources(),R.drawable.defaultalbumpic);
                        coverPhotoArrayList.add(bm);
                    }

                    albumIDArrayList.add(detailscursor.getLong(albumIDIndex));
                    albumNameArrayList.add(detailscursor.getString(albumNameIndex));
                    trackNameArrayList.add(detailscursor.getString(trackNameIndex));
                    artistNameArrayList.add(detailscursor.getString(artistNameIndex));
                    pathArrayList.add(detailscursor.getString(pathIndex));
                    sizeArrayList.add(detailscursor.getString(sizeIndex));
                    composerArrayList.add(detailscursor.getString(composerIndex));
                    durationArrayList.add(detailscursor.getString(durationIndex));
                }while(detailscursor.moveToNext());
            }
        }
    }

    public void getFavouritesFromDatabase()
    {
        albumIDArrayList.clear();
        trackNameArrayList.clear();
        pathArrayList.clear();
        coverPhotoArrayList.clear();
        albumNameArrayList.clear();
        artistNameArrayList.clear();

        Config config = new Config();
        FavouritesDB favouritesDB = new FavouritesDB(this);
        Cursor ces = favouritesDB.getAllData();
        while(ces.moveToNext())
        {
            coverPhotoArrayList.add(config.getPics(this, Long.parseLong(ces.getString(1))));
            trackNameArrayList.add(ces.getString(2));
            pathArrayList.add(ces.getString(3));
            albumNameArrayList.add(ces.getString(4));
            artistNameArrayList.add(ces.getString(5));
            composerArrayList.add(ces.getString(6));
            durationArrayList.add(config.parseTime(String.valueOf(ces.getString(7))));
            sizeArrayList.add(config.parseSize(String.valueOf(ces.getString(8))));
        }

    }
    //##############################################

    public void findAllUI()
    {
        favouriteButton = findViewById(R.id.addFavs);
        shuffleButton = findViewById(R.id.shuffleButton);
        loopButton = findViewById(R.id.loopButton);
        //albumIV = findViewById(R.id.albumImageView);
        coordinatorLayoutSongPlay = findViewById(R.id.coordinateL);
        toolbar1 = findViewById(R.id.toolbar1x);
        setSupportActionBar(toolbar1);
        playpauseButton = findViewById(R.id.playpauseFAB);
        nextButton = findViewById(R.id.playNext);
        PrevButton = findViewById(R.id.playPrevious);
        songSeekbar = findViewById(R.id.seekbarSong);
        endTimeText1 = findViewById(R.id.endTimeText);
        startTime1 = findViewById(R.id.startTimex);
        playListButton = findViewById(R.id.listButton);
        toolbar1.inflateMenu(R.menu.play_screen_menu);
        collapsingToolbarLayout = findViewById(R.id.cToolBar);
        viewPager = findViewById(R.id.viewPager);
    }

    public void activateMediaPlayer(String path)
    {
        try
        {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                //    seekBarFunction();
                }
            });

            songIsPlaying = true;
            playpauseButton.setImageResource(android.R.drawable.ic_media_pause);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        endTimeText1.setText(config.parseTime(durationArrayList.get(posOfPointerOnList)));

        //seekBarFunction(Integer.parseInt(durationArrayList.get(posOfPointerOnList)));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if(shuffleEnabled == false) {
                    Toast.makeText(MainActivity.this, "Playing next song.", Toast.LENGTH_SHORT).show();
                    playNext();
                }
                else
                {
                    posOfPointerOnList = rand.nextInt(trackNameArrayList.size());
                }
            }
        });
        checkIfFavourite(posOfPointerOnList);
    }

    public void activateSeekBarSeeking()
    {
        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.start();
            }
        });
    }

   //method that checks if a music is added to favoruites or not
    public void checkIfFavourite(int pos)
    {
       Cursor ces = favouritesDB.getAllData();
       while(ces.moveToNext())
       {
           if(!ces.getString(2).equals(trackNameArrayList.get(pos)))
           {
               favouriteButton.setImageDrawable(getResources().getDrawable(R.drawable.favourite_deselect));
               addedToFavourites = false;
           }
           else
           {
               favouriteButton.setImageDrawable(getResources().getDrawable(R.drawable.favourite_select));
               addedToFavourites = true;
           }
       }
    }


    public void startStopSong(View view) {
        if(songIsPlaying == false)
        {
            //if 0 it means that song is not playing and the button will start the song
            mediaPlayer.start();
            playpauseButton.setImageResource(android.R.drawable.ic_media_pause);
            songIsPlaying = true;
        }
        else
        {
            //if 1 then the song is playing and the song will be paused
            mediaPlayer.pause();
            playpauseButton.setImageResource(android.R.drawable.ic_media_play);
            songIsPlaying = false;
        }
    }

    public void loopSong(View view)
    {
        if(mediaPlayer.isLooping()== false)
        {
            //0 means song is not looping
            mediaPlayer.setLooping(true);
            loopButton.setImageResource(R.drawable.repeat);
            Snackbar snackbar = Snackbar.make(coordinatorLayoutSongPlay, "Looping Enabled. ", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else
        {
            mediaPlayer.setLooping(false);
            loopButton.setImageResource(R.drawable.repeat1);
            Snackbar snackbar = Snackbar.make(coordinatorLayoutSongPlay, "Looping Disabled. ", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void sleepTimeFunction()
    {
        //song will play for 30 mins and then it will stop
        if(countDownEnabled == false) {

            Snackbar snackbar = Snackbar.make(coordinatorLayoutSongPlay, "30 minutes sleep timer. ", Snackbar.LENGTH_LONG);
            snackbar.show();
            countDownEnabled = true;
            countDownTimer = new CountDownTimer(1800000, 10000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    Snackbar snackbar = Snackbar.make(coordinatorLayoutSongPlay, "Stopping all activity. Sweet Dreams. ", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    mediaPlayer.stop();
                }
            }.start();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(coordinatorLayoutSongPlay, "Sleep Timer Disabled ", Snackbar.LENGTH_LONG);
            snackbar.show();
            countDownEnabled = false;
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void playNextFunction(View view)
    {
        //play the next audio file
        playNext();
    }

    public void playPreviousFunction(View view)
    {
        //play the previous audio file
        playPrev();
    }

    public void favsFunction(View view)
    {
        //button that adds or removes  a song from favourites
        if(addedToFavourites == false) {

            favouritesDB.insertData(String.valueOf(albumIDArrayList.get(posOfPointerOnList))
                    , trackNameArrayList.get(posOfPointerOnList)
                    , pathArrayList.get(posOfPointerOnList)
                    , albumNameArrayList.get(posOfPointerOnList)
                    , artistNameArrayList.get(posOfPointerOnList)
                    , composerArrayList.get(posOfPointerOnList)
                    , durationArrayList.get(posOfPointerOnList)
                    , sizeArrayList.get(posOfPointerOnList));

            favouriteButton.setImageDrawable(getResources().getDrawable(R.drawable.favourite_select));
            addedToFavourites = true;
            Snackbar snackbar = Snackbar.make(coordinatorLayoutSongPlay,trackNameArrayList.get(posOfPointerOnList)+" has been added to your favourites! ",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else
        {
            favouritesDB.deleteData(trackNameArrayList.get(posOfPointerOnList));
            favouriteButton.setImageDrawable(getResources().getDrawable(R.drawable.favourite_deselect));
            addedToFavourites = false;
            Snackbar snackbar = Snackbar.make(coordinatorLayoutSongPlay,trackNameArrayList.get(posOfPointerOnList)+" has been removed from your favourites! ",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

//____________________________________GESTURE CONTROLS______________________________________________
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float velocityX, float velocityY) {

        if (motionEvent1.getX() - motionEvent2.getX() > 50) {
            //play previous song
            playPrev();
            return true;
        }

        else if (motionEvent2.getX() - motionEvent1.getX() > 50) {
            //play next song
            playNext();
            return true;
        }
        else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //______________________________________________________________________________________________

    public void playNext()
    {  //this method handles all the next button presses from the button and gesture controls
        int val = 0;
        if(mediaPlayer.isLooping() == true)
        {
            val = 1;
        }
        posOfPointerOnList++;
        viewPager.setCurrentItem(posOfPointerOnList,true);
        if(posOfPointerOnList == trackNameArrayList.size())
        {
            posOfPointerOnList = 0;
        }
        mediaPlayer.stop();
        mediaPlayer.reset();
        activateMediaPlayer(pathArrayList.get(posOfPointerOnList));
        collapsingToolbarLayout.setTitle(trackNameArrayList.get(posOfPointerOnList));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.cardview_dark_background));
        if(val == 1)
        {
            mediaPlayer.setLooping(true);
        }
    }

    public void playNextViewPager()
    {  //this function handles all the left and right swipes on the view pager
        int val = 0;
        if(mediaPlayer.isLooping() == true)
        {
            val = 1;
        }
        posOfPointerOnList++;
        if(posOfPointerOnList == trackNameArrayList.size())
        {
            posOfPointerOnList = 0;
        }
        mediaPlayer.stop();
        mediaPlayer.reset();
        activateMediaPlayer(pathArrayList.get(posOfPointerOnList));
        collapsingToolbarLayout.setTitle(trackNameArrayList.get(posOfPointerOnList));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.cardview_dark_background));
        if(val == 1)
        {
            mediaPlayer.setLooping(true);
        }
    }

    public void playPrev()
    {  //this function handles all the previous from the button presses
        posOfPointerOnList--;
        viewPager.setCurrentItem(posOfPointerOnList,true);
        if(posOfPointerOnList == -1)
        {
            posOfPointerOnList = trackNameArrayList.size();
        }
        mediaPlayer.stop();
        mediaPlayer.reset();
        activateMediaPlayer(pathArrayList.get(posOfPointerOnList));
        collapsingToolbarLayout.setTitle(trackNameArrayList.get(posOfPointerOnList));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.cardview_dark_background));
        //albumIV.setImageBitmap(coverPhotoArrayList.get(posOfPointerOnList));
    }

    public void shuffleFunction(View view)
    {
        EnableDisableShuffle();
    }

    public void EnableDisableShuffle()
    {
        if(shuffleEnabled)
        {
            shuffleEnabled = true;
            Snackbar snackbar = Snackbar.make(coordinatorLayoutSongPlay, "Shuffle Enabled. ", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        else
        {
            Snackbar snackbar = Snackbar.make(coordinatorLayoutSongPlay, "Shuffle Enabled. ", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    }

    public void seePlaylistFunction(View view)
    {
        final AlertDialog mBuilder = new AlertDialog.Builder(this).create();
        View myView = this.getLayoutInflater().inflate(R.layout.playlist_ui, null);
        ListView listView = myView.findViewById(R.id.playlistContentLV);
        Button button = myView.findViewById(R.id.dismiss);
        CustomPlaylistLV customPlaylistLV = new CustomPlaylistLV(this,trackNameArrayList, durationArrayList, pathArrayList);
        listView.setAdapter(customPlaylistLV);
        mBuilder.setView(myView);
        mBuilder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuilder.dismiss();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                posOfPointerOnList = position;
                int val = 0;
                if(mediaPlayer.isLooping() == true)
                {
                    val = 1;
                }
                mediaPlayer.reset();
                activateMediaPlayer(pathArrayList.get(posOfPointerOnList));
                collapsingToolbarLayout.setTitle(trackNameArrayList.get(posOfPointerOnList));
                collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.cardview_dark_background));
                //albumIV.setImageBitmap(coverPhotoArrayList.get(posOfPointerOnList));

                if(val == 1)
                {
                    mediaPlayer.setLooping(true);
                }

                mBuilder.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        favouritesDB.close();
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        System.gc();
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

}

/*

Cursor ces = favouritesDB.getAllData();
            while(ces.moveToNext())
            {
                Log.i("CHECK - 2","AlbumIDFavs "+ces.getString(1)+"\n"+
                        "tNameFavs "+ces.getString(2)+"\n"
                        +"pathnameFavs "+ces.getString(3)+"\n"
                        +"albumNameFavs "+ces.getString(4)+"\n"
                        +"artistNameFavs "+ces.getString(5)+"\n");
            }

 */