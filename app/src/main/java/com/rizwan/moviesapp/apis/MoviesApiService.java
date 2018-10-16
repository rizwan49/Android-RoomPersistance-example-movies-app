package com.rizwan.moviesapp.apis;


import com.rizwan.moviesapp.apis.model.MoviesModel;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by abdul on 14/10/18.
 * using for retrofit.
 */

public interface MoviesApiService {
    String POPULAR_URL = "3/movie/popular";
    String _SCHEME = "https";
    String IMAGE_PATH = "/image.tmdb.org/t/p/w185";


    /***
     * this method using to fetch list of movies based on pagination;
     * @param page default is 1;
     * @return list of results , totalPageCount etc;
     */
    @GET(POPULAR_URL)
    Observable<Response<MoviesModel>> getMoviesList(@Query("page") int page);

}
