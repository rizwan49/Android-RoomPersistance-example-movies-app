package com.rizwan.moviesapp;

import android.app.Application;
import android.content.Context;

public class MoviesApplication extends Application {
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getMyApplicationContext() {
        return context;
    }
}
