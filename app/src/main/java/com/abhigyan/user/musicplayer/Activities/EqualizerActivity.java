package com.abhigyan.user.musicplayer.Activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.abhigyan.user.musicplayer.R;

public class EqualizerActivity extends AppCompatActivity {

    Toolbar equalizerToolbar;
    boolean equalizerEnabled = false;
    LinearLayout linearLayoutEqualizer;

    Equalizer equalizer;
    MediaPlayer mediaPlayer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.equalizer_menu, menu);

        MenuItem switchItem = menu.findItem(R.id.switchEqualizer);
        final Switch switchView = (Switch) switchItem.getActionView();
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true)
                {
                    equalizerEnabled = true;
                    linearLayoutEqualizer.setEnabled(true);
                    Toast.makeText(EqualizerActivity.this, "Equalizer enabled", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    equalizerEnabled = false;
                    linearLayoutEqualizer.setEnabled(false);
                    Toast.makeText(EqualizerActivity.this, "Equalizer diabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        equalizerToolbar = findViewById(R.id.toolbarEqualizer);
        setSupportActionBar(equalizerToolbar);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        linearLayoutEqualizer = findViewById(R.id.equalizerLayout);

        mediaPlayer = MediaPlayer.create(this, R.raw.son);

        equalizer = new Equalizer(0,mediaPlayer.getAudioSessionId());
        equalizer.setEnabled(true);

        short nosOfBands = equalizer.getNumberOfBands();
        short lowerLimit = equalizer.getBandLevelRange()[0];
        short higherLimit = equalizer.getBandLevelRange()[1];

        for(int i = 0;i<nosOfBands;i++)
        {
            int equalizerBandIndex = i;
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView textView = new TextView(this);
         //   textView.setText();

        }

    }
}
