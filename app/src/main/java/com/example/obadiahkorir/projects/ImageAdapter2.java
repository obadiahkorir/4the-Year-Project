package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 8/29/2018.
 */
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
public class ImageAdapter2 extends BaseAdapter {
    private Context mContext;
    int imageTotal = 7;
    public static String[] mThumbIds = {
            "https://chemisoftsolutions.000webhostapp.com/android/1.jpg",
            "https://chemisoftsolutions.000webhostapp.com/android/2.jpg",
            "https://chemisoftsolutions.000webhostapp.com/android/3.jpg",
            "https://chemisoftsolutions.000webhostapp.com/android/4.jpg",
            "https://chemisoftsolutions.000webhostapp.com/android/5.jpg",
            "https://chemisoftsolutions.000webhostapp.com/android/6.jpg",
            "https://chemisoftsolutions.000webhostapp.com/android/7.jpg",
            "https://chemisoftsolutions.000webhostapp.com/android/8.jpg",
            "https://chemisoftsolutions.000webhostapp.com/android/9.jpg",
            "https://chemisoftsolutions.000webhostapp.com/android/8.jpg",
    };

    public ImageAdapter2(Context c) {
        mContext = c;
    }

    public int getCount() {
        return imageTotal;
    }

    @Override
    public String getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(480, 480));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        String url = getItem(position);
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.loader)
                .fit()
                .centerCrop().into(imageView);
        return imageView;
    }
}