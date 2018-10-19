package com.rizwan.moviesapp.apis.interceptors;

import android.util.Log;

import com.rizwan.moviesapp.MoviesApplication;
import com.rizwan.moviesapp.Utils;
import com.rizwan.moviesapp.apis.NoConnectivityException;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/***
 * this interceptor will check few condition
 * 1. it will check Internet availability if not then throw an exception which will be handle by presenter and eventBus
 * 2. if internet is available then proceed for the network call
 * 3. if in response getting 200 ie: success then returning response;
 * 3. if in response not getting 200 then passing that response code to mainScreen to show the view with the help of eventBus
 */
public class ErrorHandlerInterceptor implements Interceptor {
    static final String TAG = ErrorHandlerInterceptor.class.getName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (!Utils.isNetworkAvailable(MoviesApplication.getMyApplicationContext())) {
            Log.d(TAG, "errorHandler:internet issue");
            throw new NoConnectivityException();
        }

        Response response = chain.proceed(originalRequest);
        Boolean isSuccess = response.code() / 100 == 2 || response.code() / 100 == 3;
        if (isSuccess) {
            return response;
        }

        Log.d(TAG, "responseCode:" + response.code());
        EventBus.getDefault().post(response.code());

        return response;
    }
}
