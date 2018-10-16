package com.rizwan.moviesapp.apis.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

/**
 * Created by abdul on 14/10/18.
 */

public class MoviesInfo implements Parcelable, Comparable<MoviesInfo> {

    @Json(name = "vote_count")
    private int voteCount;

    @Json(name = "id")
    private int id;

    @Json(name = "vote_average")
    private double voteAverage;

    @Json(name = "title")
    private String title;

    @Json(name = "popularity")
    private double popularity;

    @Json(name = "poster_path")
    private String posterPath;

    @Json(name = "original_language")
    private String originalLanguage;

    @Json(name = "overview")
    private String overview;

    @Json(name = "release_date")
    private String releaseDate;

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.voteCount);
        dest.writeInt(this.id);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.title);
        dest.writeDouble(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    public MoviesInfo() {
    }

    protected MoviesInfo(Parcel in) {
        this.voteCount = in.readInt();
        this.id = in.readInt();
        this.voteAverage = in.readDouble();
        this.title = in.readString();
        this.popularity = in.readDouble();
        this.posterPath = in.readString();
        this.originalLanguage = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Parcelable.Creator<MoviesInfo> CREATOR = new Parcelable.Creator<MoviesInfo>() {
        @Override
        public MoviesInfo createFromParcel(Parcel source) {
            return new MoviesInfo(source);
        }

        @Override
        public MoviesInfo[] newArray(int size) {
            return new MoviesInfo[size];
        }
    };

    @Override
    public int compareTo(@NonNull MoviesInfo info) {
        if (info.getPopularity() > this.getPopularity())
            return 1;
        else
            return -1;
    }
}
