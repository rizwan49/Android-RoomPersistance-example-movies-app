package com.rizwan.moviesapp;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class MoviesApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Stetho.initializeWithDefaults(this);
    }

    public static Context getContext() {
        return context;
    }

}
