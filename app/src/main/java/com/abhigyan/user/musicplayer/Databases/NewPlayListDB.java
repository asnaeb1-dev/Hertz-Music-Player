package com.abhigyan.user.musicplayer.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class NewPlayListDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "playlists.db";
    private static final int DATABASE_VERSION = 1;
    public static  final String TABLE_NAME = "MY_PLAYLIST";
    private static final String COL_0 = "ID";
    private static final String COL_1 = "ALBUM_ID";
    private static final String COL_2 = "CHOSEN";
    private static final String COL_3 = "NAME";
    private static final String COL_4 = "PATH";
    private static final String COL_5 = "ALBUM_NAME";
    private static final String COL_6 = "ARTIST_NAME";


    public NewPlayListDB(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME
                +" ("+COL_0+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_1+" TEXT,"
                +COL_2+" TEXT,"
                +COL_3+" TEXT,"
                +COL_4+" TEXT,"
                +COL_5+" TEXT,"
                +COL_6+" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String album_ID, String chosen, String trackName, String path_name, String album_name, String artist_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, album_ID);
        cv.put(COL_2, chosen);
        cv.put(COL_3, trackName);
        cv.put(COL_4, path_name);
        cv.put(COL_5, album_name);
        cv.put(COL_6, artist_name);
        long result = db.insert(TABLE_NAME,null, cv);

        if(result == -1)
        {
            return  false;
        }
        else
        {
            Log.i("DONE**********", "done");
            return  true;
        }
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resources = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return resources;
    }

    public Boolean deleteData(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"NAME = ?", new String[]{ name });
        return true;
    }

    public void deleteThisDatabase(Context context,String databasename)
    {
        context.deleteDatabase(databasename);
    }
}
