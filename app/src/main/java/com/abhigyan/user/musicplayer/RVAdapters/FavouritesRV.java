package com.abhigyan.user.musicplayer.RVAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.abhigyan.user.musicplayer.Activities.MainActivity;
import com.abhigyan.user.musicplayer.Config;
import com.abhigyan.user.musicplayer.Databases.FavouritesDB;
import com.abhigyan.user.musicplayer.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavouritesRV extends RecyclerView.Adapter<FavouritesRV.Viewholder> {

    View myView;
    Context context;

    private ArrayList<String> trackNameALFavs,
                                albumNameALFavs,
                                artistNameALFavs;

    private ArrayList<Bitmap> coverPicALFavs;

    public FavouritesRV(Context context, ArrayList<String> trackNameALFavs, ArrayList<String> albumNameALFavs, ArrayList<String> artistNameALFavs, ArrayList<Bitmap> coverPicALFavs) {
        this.context = context;
        this.trackNameALFavs = trackNameALFavs;
        this.albumNameALFavs = albumNameALFavs;
        this.artistNameALFavs = artistNameALFavs;
        this.coverPicALFavs = coverPicALFavs;
    }

    @NonNull
    @Override
    public FavouritesRV.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        //create the object of the Viewholder class down below
        myView = LayoutInflater.from(context).inflate(R.layout.favourites_ui, parent, false);
        FavouritesRV.Viewholder viewholder = new FavouritesRV.Viewholder(myView);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouritesRV.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        /*populate the track names*/
        final Config config = new Config();
        if(trackNameALFavs.get(position)!=null)
        {
            holder.trackNameFavsTV.setText(config.parseTrackName(trackNameALFavs.get(position)));
        }
        else
        {
            holder.trackNameFavsTV.setText("Unknown");
        }

        /*populate the album names*/
        if(albumNameALFavs.get(position)!=null)
        {
            holder.albumNameFavsTV.setText("Album- "+config.parseTrackName(albumNameALFavs.get(position)));
        }
        else
        {
            holder.albumNameFavsTV.setText("Album- Unknown");
        }

        /*populate the artist names*/
        if(artistNameALFavs.get(position)!=null)
        {
            holder.artistNameFavsTV.setText("Artist- "+config.parseTrackName(artistNameALFavs.get(position)));
        }
        else
        {
            holder.artistNameFavsTV.setText("Album- Unknown");
        }

        /*populate the cover pics*/
        if(coverPicALFavs.get(position)!=null)
        {
            holder.coverPicCIV.setImageBitmap(coverPicALFavs.get(position));
        }
        else
        {
            holder.coverPicCIV.setImageResource(R.drawable.defaultalbumpic);
        }

        holder.songListLayoutFavs_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = (Activity) context;
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("positionpointer",position);
                intent.putExtra("callSource", 4);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        holder.optionsCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto the playsong activity

                final PopupMenu popup = new PopupMenu(context,holder.optionsCIV);
                //inflating menu from dailogxml resource
                popup.inflate(R.menu.favourites_menu_list);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.playSongMenuItem:
                                break;

                            case R.id.addToQueue:
                                break;

                            case R.id.removeFromFav:
                                FavouritesDB favouritesDB = new FavouritesDB(context);
                                favouritesDB.deleteData(trackNameALFavs.get(position));
                                trackNameALFavs.remove(position);
                                albumNameALFavs.remove(position);
                                coverPicALFavs.remove(position);
                                artistNameALFavs.remove(position);
                                notifyItemRemoved(position);

                                popup.dismiss();
                                break;

                            case R.id.details1:
                                break;

                            case R.id.addTo_playlistX:
                                break;

                            case R.id.shareFavs:
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

    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return trackNameALFavs.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ConstraintLayout songListLayoutFavs_X;
        CircleImageView coverPicCIV, optionsCIV;
        TextView trackNameFavsTV, albumNameFavsTV, artistNameFavsTV;

        public Viewholder(View itemView) {
            super(itemView);

            coverPicCIV = itemView.findViewById(R.id.favouritesCImageView);
            optionsCIV = itemView.findViewById(R.id.imageViewOptionsFavs);
            trackNameFavsTV = itemView.findViewById(R.id.trackName1Favs);
            albumNameFavsTV = itemView.findViewById(R.id.albumName1Favs);
            artistNameFavsTV = itemView.findViewById(R.id.artistName1Favs);
            songListLayoutFavs_X = itemView.findViewById(R.id.songListLayoutFavs);
        }
    }
}
