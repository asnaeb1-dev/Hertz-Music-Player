package com.abhigyan.user.musicplayer.RVAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.abhigyan.user.musicplayer.Config;
import com.abhigyan.user.musicplayer.Databases.FavouritesDB;
import com.abhigyan.user.musicplayer.Activities.MainActivity;
import com.abhigyan.user.musicplayer.Databases.MyTopTracksDB;
import com.abhigyan.user.musicplayer.Databases.NewPlayListDB;
import com.abhigyan.user.musicplayer.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongListRVAdapter extends RecyclerView.Adapter<SongListRVAdapter.Viewholder>
{
    private ArrayList<String> albumIDArrayList;
    private ArrayList<String> trackNameArrayList;
    private ArrayList<String> albumNameArrayList;
    private ArrayList<String> artistNameArrayList;
    private ArrayList<Bitmap> coverPhotoArrayList;
    private ArrayList<String> pathArrayList;
    private ArrayList<String> dataInfoArrayList;
    private ArrayList<String> durationInfoArrayList;
    private ArrayList<String> composerArrayList;

    private Context context;
    private FavouritesDB favouritesDB;
    private Config config = new Config();

    private String[] choices = {"My Playlist", "Personal Tracks", "My Top Tracks"};

    private String albumIdToBeSaved1,
                   chosenToBeSaved1,
                   tracknameToBeSaved1,
                   pathToBeSaved1,
                   albumNameToBeSaved1,
                   artistToBeSaved1;

    boolean added[];

    public SongListRVAdapter(ArrayList<String> albumIDArrayList,ArrayList<String> pathArrayList,ArrayList<String> trackNameArrayList, ArrayList<String> albumNameArrayList, ArrayList<String> artistNameArrayList, ArrayList<Bitmap> coverPhotoArrayList,ArrayList<String> dataInfoArrayList, ArrayList<String> durationInfoArrayList,ArrayList<String> composerArrayList, Context context) {

        this.albumIDArrayList = albumIDArrayList;
        this.pathArrayList = pathArrayList;
        this.trackNameArrayList = trackNameArrayList;
        this.albumNameArrayList = albumNameArrayList;
        this.artistNameArrayList = artistNameArrayList;
        this.coverPhotoArrayList = coverPhotoArrayList;
        this.dataInfoArrayList = dataInfoArrayList;
        this.durationInfoArrayList = durationInfoArrayList;
        this.composerArrayList = composerArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.song_list_ui, parent, false);
        //create the object of the Viewholder class down below
        Viewholder viewholder = new Viewholder(view);
        added = new boolean[trackNameArrayList.size()];
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        favouritesDB = new FavouritesDB(context);
        Log.i("checker********", "YES WORKING");

        if(trackNameArrayList.get(position)!=null) {

            String str = trackNameArrayList.get(position);

                holder.trackName.setText(config.parseTrackName(str));
            }
            else
            {
                holder.trackName.setText("Unknown");
            }

            if(albumNameArrayList.get(position)!=null) {


                holder.albumName.setText("Album- "+config.parseTrackName(albumNameArrayList.get(position)));
            }
            else
            {
                holder.albumName.setText("Album- Unknown");
            }

            if(artistNameArrayList.get(position)!=null) {
                holder.artistName.setText("Artist- "+ config.parseTrackName(artistNameArrayList.get(position)));
            }
            else
            {
                holder.artistName.setText("Artist- Unknown");
            }

            if(coverPhotoArrayList.get(position)!=null) {
                holder.coverPhoto.setImageBitmap(coverPhotoArrayList.get(position));
            }
            else
            {
                holder.coverPhoto.setImageResource(R.drawable.defaultalbumpic);
            }
   //This snippet checks if a song is added to favourite

        //______________________________________________________________-
            holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto the playsong activity

                final PopupMenu popup = new PopupMenu(context,holder.options);
                //inflating menu from dailogxml resource
                popup.inflate(R.menu.options_menu_list);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.markAsFav:
                                if(added[position] == false) {
                                    setFavourites(position, holder);
                                }
                                else
                                {
                                    removeFromFavs(position,holder);
                                }
                                break;

                            case R.id.details1:
                                Config config = new Config();
                                ArrayList<String> detailsarrayList = new ArrayList<>();
                                detailsarrayList.add("Track- "+trackNameArrayList.get(position));
                                detailsarrayList.add("Album- "+albumNameArrayList.get(position));
                                detailsarrayList.add("Artist- "+artistNameArrayList.get(position));
                                detailsarrayList.add("Composer- "+composerArrayList.get(position));
                                detailsarrayList.add("Path- "+pathArrayList.get(position));
                                detailsarrayList.add("Duration- "+config.parseTime(durationInfoArrayList.get(position)));
                                detailsarrayList.add("Size- "+config.parseSize(dataInfoArrayList.get(position)));

                                Activity activity = (Activity) context;
                                View detailsViewDailog = activity.getLayoutInflater().inflate(R.layout.details_dailog_ui,null);
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                                ListView listView = detailsViewDailog.findViewById(R.id.detailsLV);
                                ArrayAdapter arrayAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,detailsarrayList);
                                listView.setAdapter(arrayAdapter);

                                mBuilder.setView(detailsViewDailog);
                                mBuilder.show();

                                break;

                            case R.id.addTo:
                                popup.dismiss();
                                getPlayListDailog(position);
                                break;

                            case R.id.share:
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

                    /*Activity activity = (Activity) context;
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("positionpointer",position);
                    intent.putExtra("callSource", 1);
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);*/

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("positionpointer",position);
                    intent.putExtra("callSource", 1);
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return trackNameArrayList.size();
    }


    public void getPlayListDailog(final int pos)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.playllist_add_icon);
        builder.setTitle("Select playlist");
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                albumIdToBeSaved1 = albumIDArrayList.get(pos);
                chosenToBeSaved1 = String.valueOf(1);
                tracknameToBeSaved1 = trackNameArrayList.get(pos);
                pathToBeSaved1 = pathArrayList.get(pos);
                albumNameToBeSaved1 = albumNameArrayList.get(pos);
                artistToBeSaved1 = artistNameArrayList.get(pos);

                switch (which)
                {
                    case 0:
                        //saves data in new playlist db
                        NewPlayListDB newPlayListDB = new NewPlayListDB(context);
                        newPlayListDB.insertData(albumIdToBeSaved1,chosenToBeSaved1,tracknameToBeSaved1,pathToBeSaved1,albumNameToBeSaved1,artistToBeSaved1);
                        Toast.makeText(context,tracknameToBeSaved1+" has been saved to New Playlist.",Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        //saves data in personal db
                        //do at the end
                        break;

                    case 2:
                        //saves data in my top tracks
                        MyTopTracksDB myTopTracksDB = new MyTopTracksDB(context);
                        myTopTracksDB.insertData(albumIdToBeSaved1,chosenToBeSaved1,tracknameToBeSaved1,pathToBeSaved1,albumNameToBeSaved1,artistToBeSaved1);
                        Toast.makeText(context,tracknameToBeSaved1+" has been saved to My Top Tracks.",Toast.LENGTH_SHORT).show();

                        break;

                    default:
                        break;

                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setFavourites(int position, Viewholder holder)
    {
        favouritesDB.insertData(albumIDArrayList.get(position),
                trackNameArrayList.get(position),
                pathArrayList.get(position),
                albumNameArrayList.get(position),
                artistNameArrayList.get(position),
                composerArrayList.get(position),
                durationInfoArrayList.get(position),
                dataInfoArrayList.get(position)
                );

        Snackbar snackbar = Snackbar.make(holder.constraintLayout, trackNameArrayList.get(position)+" has been added to your favourites.", Snackbar.LENGTH_LONG);
        snackbar.show();

        holder.showOffID.setImageResource(R.drawable.favourite_select);
        added[position] = true;
    }

    public void removeFromFavs(int position, Viewholder holder)
    {
        favouritesDB.deleteData(trackNameArrayList.get(position));
        Snackbar snackbar = Snackbar.make(holder.constraintLayout, trackNameArrayList.get(position)+" has been removed from your favourites.", Snackbar.LENGTH_LONG);
        snackbar.show();
        holder.showOffID.setImageResource(R.drawable.favourite_deselect);
        added[position] = false;
    }


    public class Viewholder extends RecyclerView.ViewHolder
    {
        CircleImageView coverPhoto;
        TextView trackName, albumName, artistName;
        CircleImageView  options;
        ConstraintLayout constraintLayout;
        ImageView showOffID;

        public Viewholder(View itemView) {
            super(itemView);

            coverPhoto = itemView.findViewById(R.id.songImg);
            trackName = itemView.findViewById(R.id.trackName1);
            albumName = itemView.findViewById(R.id.albumName1);
            artistName = itemView.findViewById(R.id.artistName1);
            options = itemView.findViewById(R.id.textViewOptions);
            constraintLayout = itemView.findViewById(R.id.songListLayout);
            showOffID = itemView.findViewById(R.id.showOffID);
        }
    }


}




