package com.rizwan.moviesapp.apis.model.detail.video;

import com.squareup.moshi.Json;

public class VideoFieldModel {
    @Json(name = "id")
    String id;

    @Json(name = "iso_639_1")
    String iso_639_1;

    @Json(name = "iso_3166_1")
    String iso_3166_1;

    @Json(name = "key")
    String key;

    @Json(name = "name")
    String name;

    @Json(name = "site")
    String site;

    @Json(name = "size")
    int size;

    @Json(name = "type")
    String type;


    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
