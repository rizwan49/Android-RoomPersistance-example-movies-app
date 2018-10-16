package com.rizwan.moviesapp;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by abdul on 14/10/18.
 */

public class FontUtils {

    private static Typeface light, thin;

    public static void initFonts(Context context) {
        light = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        thin = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
    }

    public static Typeface getLight() {
        return light;
    }

    public static Typeface getThin() {
        return thin;
    }

}

