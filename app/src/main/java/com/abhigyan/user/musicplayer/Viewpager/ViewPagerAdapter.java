package com.abhigyan.user.musicplayer.Viewpager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abhigyan.user.musicplayer.R;

import java.util.ArrayList;

public class ViewPagerAdapter  extends PagerAdapter {

    Activity activity;
    LayoutInflater layoutInflater;
    ArrayList<Bitmap> images;

    public ViewPagerAdapter(Activity activity, ArrayList<Bitmap> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = layoutInflater.inflate(R.layout.view_pager_content, container, false);

        ImageView imageView = myView.findViewById(R.id.pagerAdapterIV);
        imageView.setImageBitmap(images.get(position));

        container.addView(myView);
        return myView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
