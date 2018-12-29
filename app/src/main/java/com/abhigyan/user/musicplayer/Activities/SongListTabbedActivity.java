package com.abhigyan.user.musicplayer.Activities;

/**
 MOOD PRESETS:
 1- depressed
 2- sad
 3- aimless
 4- well
 5- okay
 6- not bad
 7- good
 8- great
 9- awesome
 10- ecstatic

 TODO: change color of ui elements according to mood. keep 2 standard colors
 TODO: You CANNOT! decide colors. YOU ARE TERRIBLE AT IT! DO NOT DO IT! Ask someone experienced!
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.abhigyan.user.musicplayer.Databases.FavouritesDB;
import com.abhigyan.user.musicplayer.R;
import com.abhigyan.user.musicplayer.Viewpager.DepthTransformation;
import com.abhigyan.user.musicplayer.fragments.AlbumFragment;
import com.abhigyan.user.musicplayer.fragments.ArtistFragment;
import com.abhigyan.user.musicplayer.fragments.FavouritesFragment;
import com.abhigyan.user.musicplayer.fragments.PlaylistFragment;
import com.abhigyan.user.musicplayer.fragments.SongsFragment;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SongListTabbedActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private AppBarLayout appBarLayoutMain;
    private ViewPager mViewPager;
    private Toolbar mainToolBar;
    private TabLayout tabLayout;


    String savedColor;
    private SharedPreferences colorSharedPrefs;
    private static final String COLOR_PREFS = "color_prefs";
    Window window;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_tabbed);

        findAllUI();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        DepthTransformation depthTransformation = new DepthTransformation();
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setPageTransformer(true, depthTransformation);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        window = this.getWindow();

        colorSharedPrefs = getSharedPreferences(COLOR_PREFS,MODE_PRIVATE);
        showAccordingToDate();
        appBarLayoutMain.setBackgroundColor(Color.parseColor(colorSharedPrefs.getString("color","#0ac958")));


    }

    private void findAllUI()
    {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.containerVP);
        appBarLayoutMain = findViewById(R.id.appbarMain);
        mainToolBar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_song_list_tabbed, menu);
        MenuItem searchOption = menu.findItem(R.id.search_Option_listscreen);
        searchOption.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //search for songs
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
            case R.id.about:

                break;

            case R.id.settingsOption:
                //get all songs from the artist
                Intent intent1 = new Intent(this, MoodSetterActivity.class);
                startActivity(intent1);
                break;

            case R.id.delete:
                Toast.makeText(this, "I am active", Toast.LENGTH_SHORT).show();
                FavouritesDB favouritesDB = new FavouritesDB(this);
                //favouritesDB.close();
                favouritesDB.deleteThisDatabase(getApplicationContext(),"favourites.db");
                Toast.makeText(this, "All data has been erased!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.eq:
                Intent intent = new Intent(this,EqualizerActivity.class);
                startActivity(intent);
                break;

            case R.id.selectMood:
                generateMoodDailog();
            default:
                break;
        }
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {

                case 0:
                    //this case will take the user to the songs fragment
                    SongsFragment tab1 = new SongsFragment();
                    return tab1;

                case 1:
                    //this case will take the use to the albums frag
                    AlbumFragment tab2 = new AlbumFragment();
                    return tab2;

                case 2:
                    // this case will take the user to the artist frag
                    ArtistFragment tab3 = new ArtistFragment();

                    return tab3;

                case 3:
                    //this case will take the user to the favs frag
                    FavouritesFragment tab4 = new FavouritesFragment();

                    return tab4;

                case 4:
                    //this case will take the user to the playlists frag
                    PlaylistFragment tab5 = new PlaylistFragment();
                    return tab5;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "SONGS";

                case 1:
                    return "ALBUMS";

                case 2:
                    return "ARTIST";

                case 3:
                    return "FAVOURITES";

                case 4:
                    return "PLAYLISTS";
            }

            return super.getPageTitle(position);
        }
    }

    public void generateMoodDailog()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View myView = this.getLayoutInflater().inflate(R.layout.mood_setter_ui, null);
        alertDialog.setView(myView);
        Croller croller = myView.findViewById(R.id.croller);

        croller.setMax(10);
        croller.setMin(1);


// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {

                if(progress == 1)
                {
                    //#09090B
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#09090B"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#09090B"));
                    }
                    savedColor = "#7c7a7d";
                    croller.setLabel("Depressed");
                }
                else if(progress == 2)
                {   //#7c7a7d
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#7c7a7d"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#7c7a7d"));
                    }
                    savedColor = "#7c7a7d";
                    croller.setLabel("Sad");
                }
                else if(progress == 3)
                {
                    croller.setLabel("Aimless");
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#e0b122"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#e0b122"));
                    }
                    savedColor = "#e0b122";

                }
                else if(progress == 4)
                {
                    croller.setLabel("Well");
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#e0b122"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#e0b122"));
                    }
                    savedColor = "#e0b122";
                }
                else if(progress == 5)
                {
                    //#81b13e
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#81b13e"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#81b13e"));
                    }
                    croller.setLabel("Okay");
                    savedColor = "#81b13e";
                }
                else if(progress == 6)
                {
                    croller.setLabel("Not bad");
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#81b13e"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#81b13e"));
                    }
                    savedColor = "#81b13e";

                }
                else if(progress == 7)
                {
                    //#0970af
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#0970af"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#0970af"));
                    }
                    croller.setLabel("Good");
                    savedColor = "#0970af";
                }
                else if(progress == 8)
                {
                    croller.setLabel("Great");
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#0970af"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#1aa1f3"));
                    }
                    savedColor = "#0970af";
                }
                else if(progress == 9)
                {
                    croller.setLabel("Awesome");
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#33378d"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#33378d"));
                    }
                    savedColor = "#33378d";

                }
                else if(progress == 10)
                {
                    //#33378d
                    appBarLayoutMain.setBackgroundColor(Color.parseColor("#33378d"));
                    if(Build.VERSION.SDK_INT>=21) {
                        window.setStatusBarColor(Color.parseColor("#181a42"));
                    }
                    croller.setLabel("Ecstatic");
                    savedColor = "#33378d";
                }
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {

            }

            @Override
            public void onStopTrackingTouch(Croller croller) {

            }
        });

        Button saveEmotionButton = myView.findViewById(R.id.saveButton);
        saveEmotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences.Editor editor = colorSharedPrefs.edit();
                editor.putString("color", savedColor);
                editor.apply();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void showAccordingToDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        SharedPreferences dateSharedPrefs = getApplicationContext().getSharedPreferences("DatePref",MODE_PRIVATE);

        Log.i("datetoday*****", formattedDate);

       if(!dateSharedPrefs.getString("date","").equals(formattedDate))
        {
            Log.i("datetoday*****", formattedDate);
            SharedPreferences.Editor editor = dateSharedPrefs.edit();
            editor.putString("date",formattedDate);
            editor.apply();
            generateMoodDailog();
        }
    }

}

