package com.rizwan.moviesapp.apis;


import com.rizwan.moviesapp.apis.model.MoviesModel;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by abdul on 14/10/18.
 * using for retrofit.
 */

public interface MoviesApiService {
    String URL_POPULAR = "3/movie/popular";
    String URL_TOP_RATED = "3/movie/top_rated";

    String _SCHEME = "https";
    String IMAGE_PATH = "/image.tmdb.org/t/p/w185";


    /***
     * this method using to fetch list of movies based on pagination;
     * @param page default is 1;
     * @return list of results , totalPageCount etc;
     */
    @GET
    Observable<Response<MoviesModel>> getMoviesList(@Url String url, @Query("page") int page);

}
