package com.rizwan.moviesapp.apis.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

/**
 * Created by abdul on 14/10/18.
 */

public class MoviesInfo implements Parcelable, Comparable<MoviesInfo> {

    @Json(name = "id")
    private int id;

    @Json(name = "vote_average")
    private double voteAverage;

    @Json(name = "title")
    private String title;

    @Json(name = "poster_path")
    private String posterPath;

    @Json(name = "overview")
    private String overview;

    @Json(name = "release_date")
    private String releaseDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    public MoviesInfo() {
    }

    private MoviesInfo(Parcel in) {
        this.id = in.readInt();
        this.voteAverage = in.readDouble();
        this.title = in.readString();
        this.posterPath = in.readString();
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
        if (info.getVoteAverage() > this.getVoteAverage())
            return 1;
        else
            return -1;
    }
}
