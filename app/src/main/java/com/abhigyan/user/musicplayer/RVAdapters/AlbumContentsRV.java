package com.abhigyan.user.musicplayer.RVAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.abhigyan.user.musicplayer.Activities.MainActivity;
import com.abhigyan.user.musicplayer.R;

import java.util.ArrayList;

public class AlbumContentsRV extends RecyclerView.Adapter<AlbumContentsRV.Viewholder>
{
    private String albumName;
    private ArrayList<String> songNameArrayList;
    private ArrayList<String> durationAL;
    private ArrayList<String> artistNameAL;
    private Context context;

    //private String[] choices = {"My Playlist", "Personal Tracks", "My Top Tracks"};

    public AlbumContentsRV(ArrayList<String> songNameArrayList, ArrayList<String> durationAL,ArrayList<String> artistNameAL,String albumName, Context context) {

        this.songNameArrayList = songNameArrayList;
        this.durationAL = durationAL;
        this.artistNameAL = artistNameAL;
        this.albumName = albumName;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumContentsRV.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.album_contents_layout, parent, false);
        //create the object of the Viewholder class down below
        AlbumContentsRV.Viewholder viewholder = new AlbumContentsRV.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AlbumContentsRV.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        if(songNameArrayList.get(position)!=null) {

            holder.textView.setText("Track- "+manipulate(songNameArrayList.get(position)));
        }
        else
        {
            holder.textView.setText("Track- Unknown");
        }

        if(durationAL.get(position)!=null) {

            int firstvalue = Math.round(Integer.parseInt(durationAL.get(position))/1000);
            String seconds = String.valueOf(Math.round(firstvalue%60));
            String minutes = String.valueOf(Math.round(firstvalue/60));
            if(seconds.length()!=2)
            {
                seconds = "0"+seconds;
            }

            if(minutes.length()!=2)
            {
                minutes = "0"+minutes;
            }
            holder.textView2.setText(minutes+":"+seconds);
        }
        else
        {
            holder.textView2.setText("00:00");
        }

        if(artistNameAL.get(position)!=null)
        {
            holder.artistNameACTV.setText("Artist- "+manipulate(artistNameAL.get(position)));
        }
        else
        {
            holder.artistNameACTV.setText("Artist- Unknown");
        }

        holder.optionsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto the playsong activity

                final PopupMenu popup = new PopupMenu(context,holder.constraintLayout);
                //inflating menu from dailogxml resource
                popup.inflate(R.menu.options_menu_list);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.markAsFav:
                                //handle menu1 click
                                break;

                            case R.id.details1:
                                //handle menu2 click
                                break;

                            case R.id.addTo:
                                //handle menu3 click
                                break;

                            case R.id.share:
                                //handle menu4 click
                                break;

                            default:
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto play activity

                Activity activity = (Activity) context;
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("albumname", albumName);
                intent.putExtra("positionpointer", position);
                intent.putExtra("callSource", 2);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return songNameArrayList.size();
    }

    public String  manipulate(String str)
    {
        if(str.length()>=15)
        {
            str = str.substring(0,14)+"...";
        }
        return str;
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {

        TextView textView, artistNameACTV, textView2;
        ConstraintLayout constraintLayout;
        ImageView optionsIV;

        public Viewholder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            artistNameACTV = itemView.findViewById(R.id.artistNameACTV);
            textView2 = itemView.findViewById(R.id.textView2);
            constraintLayout = itemView.findViewById(R.id.cLayout);
            optionsIV = itemView.findViewById(R.id.imgViewx);
        }
    }
}
