package com.abhigyan.user.musicplayer.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class FavouritesDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 1;
    public static  final String TABLE_NAME = "MY_FAVORITES";
    private static final String COL_0 = "ID";
    private static final String COL_1 = "ALBUM_ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "PATH";
    private static final String COL_4 = "ALBUM_NAME";
    private static final String COL_5 = "ARTIST_NAME";
    private static final String COL_6 = "COMPOSER";
    private static final String COL_7 = "DURATION";
    private static final String COL_8 = "SIZE";


    public FavouritesDB(Context context ) {
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
                +COL_6+" TEXT,"
                +COL_7+" TEXT,"
                +COL_8+" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String album_ID, String trackName, String path_name, String album_name, String artist_name, String composer_name, String duration, String size)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, album_ID);
        cv.put(COL_2, trackName);
        cv.put(COL_3, path_name);
        cv.put(COL_4, album_name);
        cv.put(COL_5, artist_name);
        cv.put(COL_6, composer_name);
        cv.put(COL_7, duration);
        cv.put(COL_8, size);
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