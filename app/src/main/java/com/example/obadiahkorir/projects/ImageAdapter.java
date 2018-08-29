package com.example.obadiahkorir.projects;

/**
 * Created by CHEMISOFT on 2/18/2018.
 */
import com.android.volley.Cache;
import android.support.v4.util.LruCache;
import com.android.volley.Network;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import android.graphics.Bitmap;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.RequestQueue;
import android.content.Context;;
import com.android.volley.toolbox.DiskBasedCache;
public class ImageAdapter {

    public static ImageAdapter imageAdapter;

    public Network networkOBJ ;

    public RequestQueue requestQueue1;

    public ImageLoader Imageloader1;

    public Cache cache1 ;

    public static Context context1;

    LruCache<String, Bitmap> LRUCACHE = new LruCache<String, Bitmap>(30);

    private ImageAdapter(Context context) {

        this.context1 = context;

        this.requestQueue1 = RequestQueueFunction();

        Imageloader1 = new ImageLoader(requestQueue1, new ImageLoader.ImageCache() {

            @Override
            public Bitmap getBitmap(String URL) {

                return LRUCACHE.get(URL);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

                LRUCACHE.put(url, bitmap);
            }
        });
    }

    public ImageLoader getImageLoader() {

        return Imageloader1;
    }

    public static ImageAdapter getInstance(Context SynchronizedContext) {

        if (imageAdapter == null) {

            imageAdapter = new ImageAdapter(SynchronizedContext);
        }
        return imageAdapter;
    }

    public RequestQueue RequestQueueFunction() {

        if (requestQueue1 == null) {

            cache1 = new DiskBasedCache(context1.getCacheDir());

            networkOBJ = new BasicNetwork(new HurlStack());

            requestQueue1 = new RequestQueue(cache1, networkOBJ);

            requestQueue1.start();
        }
        return requestQueue1;
    }
}