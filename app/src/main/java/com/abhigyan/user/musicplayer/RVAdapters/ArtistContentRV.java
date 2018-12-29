package com.abhigyan.user.musicplayer.RVAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhigyan.user.musicplayer.Activities.MainActivity;
import com.abhigyan.user.musicplayer.Config;
import com.abhigyan.user.musicplayer.R;

import java.util.ArrayList;

public class ArtistContentRV extends RecyclerView.Adapter<ArtistContentRV.Viewholder>
{
    private String artistName;
    private ArrayList<String> songNameArrayList;
    private ArrayList<String> durationAL;
    private ArrayList<String> albumNameAL;
    private Context context;

    public ArtistContentRV(ArrayList<String> songNameArrayList, ArrayList<String> durationAL,ArrayList<String> albumNameAL,String artistName, Context context) {

        this.songNameArrayList = songNameArrayList;
        this.durationAL = durationAL;
        this.albumNameAL = albumNameAL;
        this.artistName = artistName;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistContentRV.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.artist_content_ui, parent, false);
        //create the object of the Viewholder class down below
        ArtistContentRV.Viewholder viewholder = new ArtistContentRV.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtistContentRV.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        final Config config = new Config();

        if(songNameArrayList.get(position)!=null) {

            holder.trackNameTV.setText("Track- "+config.parseTrackName(songNameArrayList.get(position)));
        }
        else
        {
            holder.trackNameTV.setText("Track- Unknown");
        }

        if(durationAL.get(position)!=null) {

            holder.durationTV.setText(config.parseTime(durationAL.get(position)));
        }
        else
        {
            holder.durationTV.setText("00:00");
        }

        if(albumNameAL.get(position)!=null)
        {
            holder.albumNameTV.setText("Album- "+config.parseTrackName(albumNameAL.get(position)));
        }
        else
        {
            holder.albumNameTV.setText("Album- Unknown");
        }


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto play activity
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("callSource", 3);
                intent.putExtra("positionpointer",position);
                intent.putExtra("artistName", artistName);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return songNameArrayList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder
    {

        TextView trackNameTV, albumNameTV, durationTV;
        ConstraintLayout constraintLayout;

        public Viewholder(View itemView) {
            super(itemView);

            trackNameTV = itemView.findViewById(R.id.trackNameTVACV);
            albumNameTV = itemView.findViewById(R.id.albumNameTVACV);
            durationTV = itemView.findViewById(R.id.durationTVACV);
            constraintLayout = itemView.findViewById(R.id.layoutUIArtistConstraint);
        }
    }
}
