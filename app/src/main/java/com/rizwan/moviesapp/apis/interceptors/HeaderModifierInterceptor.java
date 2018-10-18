package com.rizwan.moviesapp.apis.interceptors;

import android.util.Log;

import com.rizwan.moviesapp.constants.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * this is header interceptor which will help to set the header into each api call;
 */

public class HeaderModifierInterceptor implements Interceptor {
    private static final String API_KEY = "api_key";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCEPT = "Accept";

    private final String TAG = this.getClass().getName();

    public HeaderModifierInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl url = originalRequest.url().newBuilder().addQueryParameter(API_KEY, Constants.KEY).build();

        Log.d(TAG, " setup header modifier");
        Request modifiedRequest = originalRequest.newBuilder().url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(ACCEPT, APPLICATION_JSON)
                .build();

        return chain.proceed(modifiedRequest);
    }
}

