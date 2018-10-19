package com.rizwan.moviesapp.apis;

import android.util.Log;

import com.rizwan.moviesapp.MoviesApplication;
import com.rizwan.moviesapp.R;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    private final String TAG = NoConnectivityException.class.getName();

    public NoConnectivityException() {
        Log.e(TAG, MoviesApplication.getMyApplicationContext().getString(R.string.no_connectivity_exception_msg));
    }
}
