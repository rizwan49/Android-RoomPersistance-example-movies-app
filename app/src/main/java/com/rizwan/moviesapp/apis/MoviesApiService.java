package com.rizwan.moviesapp.apis;


import com.rizwan.moviesapp.apis.model.MoviesModel;
import com.rizwan.moviesapp.apis.model.detail.VideosAndReviewsModel;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by abdul on 14/10/18.
 * using for retrofit.
 */

public interface MoviesApiService {
    String KEY = "63f94c3c59156193af87f35751ad0bdf";
    String BASE_URL = "https://api.themoviedb.org";
    String MIDDLE_URL = "/3/movie";
    String URL_POPULAR = MIDDLE_URL + "/popular";
    String URL_TOP_RATED = MIDDLE_URL + "/top_rated";
    String _SCHEME = "https";
    String IMAGE_PATH = "/image.tmdb.org/t/p/w185";
    String PAGE = "page";
    String ID_PATH = "/{id}";
    String REVIEWS = ID_PATH + "/reviews";
    String VIDEOS = ID_PATH + "/videos";

    /***
     * this method is used by the retrofit;
     * @param url dynamically passing the required url, user can use the sort menu option then the url will get change;
     * @param page based on the page we get the response;
     */
    @GET
    Observable<Response<MoviesModel>> getMoviesList(@Url String url, @Query(PAGE) int page);

    @GET(BASE_URL + MIDDLE_URL + VIDEOS)
    Observable<Response<VideosAndReviewsModel>> getVideos(@Path("id") int id);

    @GET(BASE_URL + MIDDLE_URL + REVIEWS)
    Observable<Response<VideosAndReviewsModel>> getReviews(@Path("id") int id);
}
