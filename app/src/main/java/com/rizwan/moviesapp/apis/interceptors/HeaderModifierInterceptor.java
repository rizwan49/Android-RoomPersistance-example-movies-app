package com.rizwan.moviesapp.apis.interceptors;

import android.util.Log;

import com.rizwan.moviesapp.apis.MoviesApiService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * header interceptor which will help to set the header information into each api call;
 */

public class HeaderModifierInterceptor implements Interceptor {
    private static final String API_KEY = "api_key";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCEPT = "Accept";

    private final String TAG = HeaderModifierInterceptor.class.getName();

    public HeaderModifierInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl url = originalRequest.url().newBuilder().addQueryParameter(API_KEY, MoviesApiService.KEY).build();

        Log.d(TAG, " setup header modifier");
        Request modifiedRequest = originalRequest.newBuilder().url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(ACCEPT, APPLICATION_JSON)
                .build();

        return chain.proceed(modifiedRequest);
    }
}

