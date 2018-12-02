package com.rizwan.moviesapp.apis.model.detail;

import com.rizwan.moviesapp.apis.model.detail.review.ReviewFieldModel;
import com.squareup.moshi.Json;

import java.util.List;

public class VideosAndReviewsModel {
    @Json(name = "id")
    private int id;

    @Json(name = "results")
    private List<ReviewFieldModel> results;

    @Json(name = "page")
    private int page;

    @Json(name = "total_pages")
    private int totalPages;

    @Json(name = "total_results")
    private int totalResults;

    public int getId() {
        return id;
    }

    public List<ReviewFieldModel> getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
