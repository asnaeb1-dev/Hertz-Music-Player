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
import android.widget.PopupMenu;
import android.widget.TextView;

import com.abhigyan.user.musicplayer.Activities.ArtistContentActivity;
import com.abhigyan.user.musicplayer.Activities.MainActivity;
import com.abhigyan.user.musicplayer.Config;
import com.abhigyan.user.musicplayer.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistRVAdater extends RecyclerView.Adapter<ArtistRVAdater.Viewholder> {

    private ArrayList<String> artistNameARV;
    private ArrayList<String> albumNameARV;

    private Context context;

    public ArtistRVAdater(ArrayList<String> artistNameARV, ArrayList<String> albumNameARV, Context context) {

        this.artistNameARV = artistNameARV;
        this.albumNameARV = albumNameARV;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistRVAdater.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.artist_list_ui, parent, false);
        //create the object of the Viewholder class down below
        ArtistRVAdater.Viewholder viewholder = new ArtistRVAdater.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtistRVAdater.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        final Config config = new Config();

        if (artistNameARV.get(position) != null) {

            holder.artistNameARV.setText("Artist- " + config.parseTrackName(artistNameARV.get(position)));
        }
        else
            {
            holder.artistNameARV.setText("Artist- Unknown");
        }

        if (albumNameARV.get(position) != null) {

            holder.albumNameARV.setText("Album- " + config.parseTrackName(albumNameARV.get(position)));

        }
        else
        {
            holder.albumNameARV.setText("Album- Unknown");
        }

        holder.circleImageViewx.setImageResource(R.drawable.defaultalbumpic);

        holder.artistUIoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context,holder.artistUIoptions);
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

        holder.constraintLayoutARV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto play activity

                Activity activity = (Activity) context;
                Intent intent = new Intent(context, ArtistContentActivity.class);
                intent.putExtra("artistName", artistNameARV.get(position));
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });
    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return artistNameARV.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView artistNameARV, albumNameARV;
        ConstraintLayout constraintLayoutARV;
        CircleImageView circleImageViewx, artistUIoptions;

        public Viewholder(View itemView) {
            super(itemView);

            artistNameARV = itemView.findViewById(R.id.artistNameARVx);
            albumNameARV = itemView.findViewById(R.id.albumNameARVx);
            constraintLayoutARV = itemView.findViewById(R.id.cLayoutARVx);
            circleImageViewx = itemView.findViewById(R.id.artistImgARVx);
            artistUIoptions = itemView.findViewById(R.id.artistuioptions);
        }
    }
}
