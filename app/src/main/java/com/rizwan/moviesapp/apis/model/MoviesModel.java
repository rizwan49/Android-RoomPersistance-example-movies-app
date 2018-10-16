package com.rizwan.moviesapp.apis.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 14/10/18.
 */

public class MoviesModel implements Parcelable {
    @Json(name = "results")
    List<MoviesInfo> moviesList;

    @Json(name = "page")
    int page;

    @Json(name = "total_results")
    int totalItems;

    @Json(name = "total_pages")
    int totalPages;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.moviesList);
        dest.writeInt(this.page);
        dest.writeInt(this.totalItems);
        dest.writeInt(this.totalPages);
    }

    public MoviesModel() {
    }

    protected MoviesModel(Parcel in) {
        this.moviesList = new ArrayList<>();
        in.readList(this.moviesList, MoviesInfo.class.getClassLoader());
        this.page = in.readInt();
        this.totalItems = in.readInt();
        this.totalPages = in.readInt();
    }

    public static final Parcelable.Creator<MoviesModel> CREATOR = new Parcelable.Creator<MoviesModel>() {
        @Override
        public MoviesModel createFromParcel(Parcel source) {
            return new MoviesModel(source);
        }

        @Override
        public MoviesModel[] newArray(int size) {
            return new MoviesModel[size];
        }
    };

    public List<MoviesInfo> getUpcomingMoviesList() {
        return moviesList;
    }

    public void setUpcomingMoviesList(List<MoviesInfo> moviesList) {
        this.moviesList = moviesList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
