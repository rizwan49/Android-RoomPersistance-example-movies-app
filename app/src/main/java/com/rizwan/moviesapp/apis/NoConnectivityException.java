package com.rizwan.moviesapp.apis;

import android.content.Context;
import android.util.Log;

import com.rizwan.moviesapp.R;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    private final String TAG = NoConnectivityException.class.getName();

    public NoConnectivityException(Context context) {
        if (context != null)
            Log.e(TAG, context.getString(R.string.no_connectivity_exception_msg));
    }
}
