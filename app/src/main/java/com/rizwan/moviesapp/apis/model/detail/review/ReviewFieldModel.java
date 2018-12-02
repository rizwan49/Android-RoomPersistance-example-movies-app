package com.rizwan.moviesapp.apis.model.detail.review;

import com.rizwan.moviesapp.apis.model.detail.video.VideoFieldModel;
import com.squareup.moshi.Json;

public class ReviewFieldModel extends VideoFieldModel{

    @Json(name = "auther")
    private String auther;

    @Json(name = "content")
    private String content;

    @Json(name = "url")
    private String url;


    public String getAuther() {
        return auther;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
