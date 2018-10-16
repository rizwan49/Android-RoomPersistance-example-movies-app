package com.rizwan.moviesapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by abdul on 15/10/18.
 */

public class Utils {
    private static String TAG = "Utils";

    public static void hideViews(View... views) {
        for (View view : views) {
            if (view == null) continue;
            view.setVisibility(View.GONE);
        }
    }

    public static void showViews(View... views) {
        for (View view : views) {
            if (view == null) continue;
            view.setVisibility(View.VISIBLE);
        }
    }


    public static void loadImage(Context context, ImageView imageView, Uri url, int errorImageUrl) {
        Picasso.with(context)
                .load(url)
                .error(errorImageUrl)
                .into(imageView);

    }

    /***
     * @param context passing to get the network status
     * @return boolean value as true or false
     * true: if network is available
     * false: if not available
     */
    public static boolean isNetworkAvailable(final Context context) {
        if (context == null) return false;
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager.getActiveNetworkInfo() != null) {
            try {
                return connectivityManager.getActiveNetworkInfo().isConnected();
            } catch (Exception e) {
            }
        }
        return false;
    }

}
