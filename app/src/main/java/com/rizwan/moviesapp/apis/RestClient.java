package com.rizwan.moviesapp.apis;


import com.rizwan.moviesapp.BuildConfig;
import com.rizwan.moviesapp.apis.interceptors.ErrorHandlerInterceptor;
import com.rizwan.moviesapp.apis.interceptors.HeaderModifierInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by abdul on 14/10/18.
 * 1. created RestClient to setup once, which will improve performance and help to make a server call;
 * 2. added LogInterceptor only into debug mode;
 * 3. added ErrorHandlerInterceptor
 * 4. added HeaderModifier
 */

public class RestClient {
    private static MoviesApiService apiService;

    private RestClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.interceptors().add(new HeaderModifierInterceptor());
        httpClient.interceptors().add(new ErrorHandlerInterceptor());
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(logging);
        }

        Retrofit restAdapter = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(MoviesApiService.BASE_URL)//passing API_URL
                .addConverterFactory(MoshiConverterFactory.create()) //passing MoshiConverterFactory to convert json key and value into our object
                .client(httpClient.build())//passing OkHttpClient object
                .build();
        apiService = restAdapter.create(MoviesApiService.class);
    }


    //double checked locking singleTon Design.
    public static MoviesApiService getApiService() {
        if (apiService == null) {
            synchronized (RestClient.class) {
                if (apiService == null)
                    new RestClient();
            }
        }
        return apiService;
    }

}
