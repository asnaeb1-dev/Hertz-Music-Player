package com.abhigyan.user.musicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.text.DecimalFormat;

public class Config {

     static final String APPNAME ="Hertz";
     static final String ABOUTACTIVITYNAME = "About";
     static final String YOUTUBEAPIKEY = "AIzaSyAOhfkqDr8LT_AsIxzBsUHv-R-AlDAaDGs";

     public String parseTime(String time)
     {
          int firstvalue = Integer.parseInt(time);
          firstvalue = Math.round(firstvalue/1000);
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

          return  String.valueOf(minutes)+":"+String.valueOf(seconds);
     }

     public String parseSize(String size)
     {
          float data = Float.parseFloat(size);
          float mbs = data/(1024*1024);

          DecimalFormat df = new DecimalFormat("###.##");
          String mbs1 = String.valueOf(df.format(mbs));

          return mbs1+" MB";
     }

     public String  parseTrackName(String str)
     {
          if(str.length()>=15)
          {
               str = str.substring(0,15)+"...";
          }
          return str;
     }

     public Bitmap getPics(Context context, long albumID)
     {
          Bitmap bm = null;
          try
          {
               final Uri sArtworkUri = Uri
                       .parse("content://media/external/audio/albumart");

               Uri uri = ContentUris.withAppendedId(sArtworkUri, albumID);

               ParcelFileDescriptor pfd = context.getContentResolver()
                       .openFileDescriptor(uri, "r");

               if (pfd != null)
               {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);

               }
          } catch (Exception e) {
               e.printStackTrace();
               bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.defaultalbumpic);

          }
          return bm;
     }
}
