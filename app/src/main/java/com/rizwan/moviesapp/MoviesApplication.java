package com.rizwan.moviesapp;

import android.app.Application;
import android.content.Context;

public class MoviesApplication extends Application {
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

}
