package com.quikr.partho.quikrchallenge;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by partho on 22/8/15.
 */
public class QuikrCarHubApplication extends Application {
    private static QuikrCarHubApplication sInstance;
    private RequestQueue appRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        appRequestQueue = Volley.newRequestQueue(getApplicationContext());
        if (sInstance == null)
        {
            sInstance = this;
        }
    }

    public synchronized static QuikrCarHubApplication getInstance()
    {
        return sInstance;
    }

    public RequestQueue getAppRequestQueue()
    {
        return appRequestQueue;
    }
}
