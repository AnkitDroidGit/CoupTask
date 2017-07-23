package com.ankit.couptask.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * @author by Ankit Kumar (ankitdroiddeveloper@gmail.com).
 */

public class CoupVolleyRequestQueue {
    private static final String TAG = CoupVolleyRequestQueue.class.getSimpleName();
    private static CoupVolleyRequestQueue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    //private ImageLoader mImageLoader;

    private CoupVolleyRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized CoupVolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CoupVolleyRequestQueue(context);
        }
        return mInstance;
    }

    public static synchronized CoupVolleyRequestQueue getInstance() {
        if (mInstance == null) {
            mInstance = new CoupVolleyRequestQueue(null); // don't use cache.
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        // use volley only for Http/Https handling. no caching.
        req.setShouldCache(false);

        getRequestQueue().add(req);
    }


}
