package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 8/29/2018.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.InputStream;
public class FullImageActivity extends Activity {

    ProgressDialog pDialog;
    ImageView img;
    Bitmap bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimageview);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        ImageAdapter2 imageAdapter2 = new ImageAdapter2(this);

        img = (ImageView) findViewById(R.id.image);
        String url = imageAdapter2.getItem(position);

        new DownloadImage().execute(url);
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL = URL[0];
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            img.setImageBitmap(result);
        }
    }

}