package com.rizwan.moviesapp.apis.model;

import com.squareup.moshi.Json;
import java.util.List;

/**
 * Created by abdul on 14/10/18.
 */

public class MoviesModel {
    @Json(name = "results")
    private List<MoviesInfo> moviesList;

    @Json(name = "page")
    private int page;

    @Json(name = "total_results")
    private int totalItems;

    @Json(name = "total_pages")
    private int totalPages;

    public List<MoviesInfo> getMoviesList() {
        return moviesList;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
